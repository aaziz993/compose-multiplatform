package gradle.api

import gradle.accessors.settings
import gradle.api.artifacts.DependencyNotation
import gradle.api.artifacts.PluginNotation
import gradle.get
import klib.data.type.primitive.isPath
import klib.data.type.primitive.isValidUrl
import klib.data.type.serialization.json.decodeFromAny
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.initialization.Settings
import org.jetbrains.kotlin.gradle.plugin.extraProperties
import org.tomlj.Toml
import org.tomlj.TomlTable

@Serializable
internal data class VersionCatalog(
    val name: String,
    val versions: Map<String, String> = emptyMap(),
    val libraries: Map<String, DependencyNotation> = emptyMap(),
    val plugins: Map<String, PluginNotation> = emptyMap(),
    val bundles: Map<String, List<DependencyNotation>> = emptyMap(),
) {

    companion object {

        private val json = Json {
            ignoreUnknownKeys = true
        }

        @Suppress("UNCHECKED_CAST")
        operator fun invoke(name: String, value: String): VersionCatalog =
            json.decodeFromAny(
                mapOf("name" to name) +
                    Toml.parse(value).let { toml ->
                        val versions = toml["versions"] as TomlTable

                        val libraries = (toml["libraries"] as TomlTable).toAliasMap().resolveVersions(versions)

                        mapOf(
                            "versions" to versions.toAliasMap(),
                            "libraries" to libraries,
                            "bundles" to (toml["bundles"] as TomlTable).toAliasMap().mapValues { (_, references) ->
                                (references as List<String>)
                                    .map(String::asVersionCatalogAlias)
                                    .map(libraries::get)
                            },
                            "plugins" to (toml["plugins"] as TomlTable).toAliasMap().resolveVersions(versions),
                        )
                    },
            )

        private fun TomlTable.toAliasMap() = toMap().mapKeys { (key, _) -> key.asVersionCatalogAlias }

        @Suppress("UNCHECKED_CAST")
        private fun Map<String, Any>.resolveVersions(versions: TomlTable) =
            toMutableMap().mapValues { (_, value) ->
                value as Map<String, Any>
                if (value["version"] is String) value
                else versions.getString((value["version"] as Map<String, Any>)["ref"] as String)
            }
    }
}

private val String.asVersionCatalogAlias
    get() = replace(".", "-")

private const val VERSION_CATALOG_EXT = "version.catalog.ext"

@Suppress("UNCHECKED_CAST")
internal var Settings.allLibs: Set<VersionCatalog>
    get() = extraProperties.get(VERSION_CATALOG_EXT) as MutableSet<VersionCatalog>
    set(value) {
        extraProperties[VERSION_CATALOG_EXT] = value
    }

internal val Settings.libs: VersionCatalog
    get() = allLibs["libs"]!!

private operator fun Set<VersionCatalog>.get(name: String) =
    find { versionCatalog -> versionCatalog.name == name }

context(Settings)
@Suppress("UnstableApiUsage")
internal val DependencyNotation.notation
    get() = settings.allLibs.resolveDependency(
        toString(),
        settings.layout.settingsDirectory,
    )

private fun Set<VersionCatalog>.resolveDependency(
    notation: String,
    directory: Directory,
    project: (name: String) -> Project = { name -> throw UnsupportedOperationException("Can't resolve project: '$name'") }
): Any = when {
    notation.startsWith("$") -> resolveVersionCatalogReference(notation.removePrefix("$"), VersionCatalog::libraries).toString()
    notation.startsWith(":") -> project(notation)
    notation.isPath -> directory.files(notation)
    notation.isValidUrl -> notation.asVersionCatalogUrl
    else -> notation
}

private val String.asVersionCatalogUrl: String
    get() {
        val fileNamePart = substringAfter(":", "").replace(":", "-")

        return "${substringBeforeLast(":").replace("[.:]".toRegex(), "/")}/${
            substringAfterLast(":", "")
        }/$fileNamePart.toml"
    }

context(Settings)
@Suppress("UnstableApiUsage")
internal fun PluginNotation.resolve() = settings.allLibs.resolve(
    toString(),
    settings.layout.settingsDirectory,
)

context(Project)
internal fun String.resolveVersion() =
    if (startsWith("$")) project.settings.allLibs.resolveVersionCatalogReference(removePrefix("$"), VersionCatalog::versions)!!
    else this

private fun <T> Set<VersionCatalog>.resolveVersionCatalogReference(
    reference: String,
): T? {
    val versionCatalogName = reference.substringBefore(".")
    val keys = reference.substringAfter(".", "").split(".").toTypedArray()

    return (this[versionCatalogName]
        ?: error("Not found version catalog: $versionCatalogName")).get(* keys)
}

