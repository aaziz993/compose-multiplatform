package gradle.api.initialization.dsl

import klib.data.type.collections.list.asList
import klib.data.type.collections.map.asMap
import klib.data.type.collections.takeUnlessEmpty
import klib.data.type.functions.tryInvoke
import klib.data.type.serialization.serializers.any.AnySerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.peanuuutz.tomlkt.Toml
import org.gradle.api.Project
import org.gradle.api.artifacts.ExternalModuleDependencyBundle
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.MutableVersionConstraint
import org.gradle.api.artifacts.VersionConstraint
import org.gradle.api.artifacts.repositories.IvyArtifactRepository
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.file.FileCollection
import org.gradle.api.initialization.Settings
import org.gradle.api.internal.artifacts.DefaultModuleIdentifier
import org.gradle.api.internal.artifacts.dependencies.DefaultMutableMinimalDependency
import org.gradle.api.internal.artifacts.dependencies.DefaultMutableVersionConstraint
import org.gradle.api.internal.artifacts.dependencies.DefaultPluginDependency
import org.gradle.api.internal.catalog.DefaultExternalModuleDependencyBundle
import org.gradle.api.provider.Provider
import org.gradle.plugin.use.PluginDependency

public const val VERSION_CATALOG_DIR: String = "gradle"

@Serializable(with = VersionCatalogSerializer::class)
public data class VersionCatalog(
    public val versions: Map<String, VersionConstraint>,
    private val libraries: Map<String, MinimalExternalModuleDependency>,
    private val plugins: Map<String, PluginDependency>,
    private val bundles: Map<String, ExternalModuleDependencyBundle>
) {

    public fun versions(alias: String): VersionConstraint =
        requireNotNull(versions[alias]) { "Unresolved version '$alias'" }

    public fun libraries(alias: String): MinimalExternalModuleDependency =
        requireNotNull(libraries[alias]) { "Unresolved library '$alias'" }

    public operator fun get(alias: String): MinimalExternalModuleDependency = libraries(alias)

    public fun plugins(alias: String): PluginDependency =
        requireNotNull(plugins[alias]) { "Unresolved plugin '$alias'" }

    context(project: Project)
    public fun bundles(alias: String): Provider<ExternalModuleDependencyBundle> =
        project.objects.property(ExternalModuleDependencyBundle::class.java).apply {
            set(requireNotNull(bundles[alias]) { "Unresolved bundle '$alias'" })
        }

    public companion object {

        @Suppress("UnstableApiUsage")
        context(settings: Settings)
        public operator fun invoke(dependencyNotation: Any): VersionCatalog =
            when (dependencyNotation) {
                is FileCollection -> Toml.decodeFromString<VersionCatalog>(dependencyNotation.files.single().readText())
                is MinimalExternalModuleDependency -> {
                    val file = settings.layout.settingsDirectory.dir(VERSION_CATALOG_DIR).file(".$dependencyNotation.toml").asFile

                    val text = if (file.exists()) file.readText()
                    else settings.dependencyResolutionManagement.repositories.firstNotNullOfOrNull { repository ->
                        when (repository) {
                            is MavenArtifactRepository -> repository.url
                            is IvyArtifactRepository -> repository.url
                            else -> null
                        }?.let { url ->
                            runCatching {
                                url.resolve(dependencyNotation.toString().toCatalogUrl()).toURL().readText()
                            }.getOrNull()
                        }
                    }?.also(file::writeText) ?: error("Couldn't find version catalog '$dependencyNotation'")

                    Toml.decodeFromString<VersionCatalog>(text)
                }

                else -> throw IllegalArgumentException("Unknown dependency notation '$dependencyNotation'")
            }

        private fun String.toCatalogUrl(): String {
            val fileNamePart = substringAfter(":", "").replace(":", "-")

            return "${substringBeforeLast(":").replace("[.:]".toRegex(), "/")}/${
                substringAfterLast(":", "")
            }/$fileNamePart.toml"
        }
    }
}

private object VersionCatalogSerializer : KSerializer<VersionCatalog> {

    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("VersionCatalog") {
            element("versions", MapSerializer(String.serializer(), String.serializer()).descriptor)
            element("libraries", MapSerializer(String.serializer(), String.serializer()).descriptor)
            element("plugins", MapSerializer(String.serializer(), String.serializer()).descriptor)
            element("bundles", MapSerializer(String.serializer(), String.serializer()).descriptor)
        }

    override fun serialize(encoder: Encoder, value: VersionCatalog) = throw UnsupportedOperationException()

    @Suppress("UNCHECKED_CAST")
    override fun deserialize(decoder: Decoder): VersionCatalog {
        val value = decoder.decodeSerializableValue(
            MapSerializer(String.serializer(), AnySerializer),
        )

        val versions =
            value["versions"]?.asMap.orEmpty().mapValues { (_, version) -> mutableVersionConstraint(version) }

        val libraries = value["libraries"]?.asMap.orEmpty().mapValues { (_, value) ->
            value as Map<String, Any>

            val (group, name) = if (value.containsKey("module"))
                (value["module"] as String).split(":").let { (group, name) -> group to name }
            else value["group"] as String to value["name"] as String

            DefaultMutableMinimalDependency(
                DefaultModuleIdentifier.newId(group, name),
                resolveVersion(value["version"], versions),
                null,
            )
        }

        return VersionCatalog(
            versions.toCatalogAliasMap(),
            libraries.toCatalogAliasMap(),
            value["plugins"]?.asMap.orEmpty().mapValues { (_, plugin) ->
                plugin as Map<String, Any>

                DefaultPluginDependency(
                    plugin["id"] as String,
                    resolveVersion(plugin["version"], versions),
                )
            }.toCatalogAliasMap(),
            value["bundles"]?.asMap.orEmpty().mapValues { (_, references) ->
                DefaultExternalModuleDependencyBundle().apply {
                    addAll(
                        references.asList<String>().map { reference ->
                            libraries[reference] ?: throw IllegalArgumentException("Unknown library $reference")
                        },
                    )
                }
            }.toCatalogAliasMap(),
        )
    }

    @Suppress("UNCHECKED_CAST")
    private fun mutableVersionConstraint(version: Any?) = when (version) {
        null, is String -> DefaultMutableVersionConstraint(version.orEmpty())

        else -> {
            version as Map<String, Any>

            DefaultMutableVersionConstraint(version["require"] as String).apply {
                ::setBranch tryInvoke version["branch"] as String?
                ::prefer tryInvoke (version["prefer"] as String?)?.takeIf(String::isNotBlank)
                ::strictly tryInvoke (version["strictly"] as String?)?.takeIf(String::isNotBlank)
                ::reject tryInvoke version["reject"]?.asList<String>()?.takeUnlessEmpty()?.toTypedArray()
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun resolveVersion(
        version: Any?,
        versions: Map<String, MutableVersionConstraint>
    ): MutableVersionConstraint =
        when (version) {
            null, is String -> DefaultMutableVersionConstraint(version.orEmpty())
            else -> requireNotNull(versions[version.asMap["ref"]]) { "Unresolved version ref '${version.asMap["ref"]}'" }
        }

    private fun <V> Map<String, V>.toCatalogAliasMap() = mapKeys { (key, _) -> key.toCatalogAlias() }

    private fun String.toCatalogAlias() = replace("-", ".")
}
