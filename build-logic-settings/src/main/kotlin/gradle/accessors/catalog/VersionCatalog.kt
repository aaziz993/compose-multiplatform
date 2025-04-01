package gradle.accessors.catalog

import gradle.accessors.settings
import klib.data.type.isPath
import klib.data.type.isValidUrl
import klib.data.type.serialization.decodeFromAny
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.initialization.Settings
import org.jetbrains.kotlin.gradle.plugin.extraProperties
import org.tomlj.TomlParseResult

@Serializable
internal data class VersionCatalog(
    val name: String,
    val versions: Map<String, String> = emptyMap(),
    val libraries: Map<String, Library> = emptyMap(),
    val plugins: Map<String, Plugin> = emptyMap()
) {

    init {
        libraries.values.forEach { library ->
            library.versionCatalog = this
        }

        plugins.values.forEach { plugin ->
            plugin.versionCatalog = this
        }
    }

    fun versionOrNull(alias: String) = versions[alias.asVersionCatalogAlias]
    fun version(alias: String) =
        versionOrNull(alias) ?: error("Version '$alias' not found in version catalog: $name")

    fun library(alias: String): Library = alias.asVersionCatalogAlias.let { alias ->
        libraries[alias] ?: error("Library  '$alias'  not found in version catalog: $name")
    }

    fun plugin(alias: String): Plugin = alias.asVersionCatalogAlias.let { alias ->
        plugins[alias] ?: error("Plugin '$alias' not found in version catalog: $name")
    }
}

private val json = Json {
    ignoreUnknownKeys = true
}

internal fun TomlParseResult.toVersionCatalog(name: String): VersionCatalog =
    json.decodeFromAny(toMap() + ("name" to name))

private const val VERSION_CATALOG_EXT = "version.catalog.ext"

@Suppress("UNCHECKED_CAST")
internal var Settings.allLibs: Set<VersionCatalog>
    get() = extraProperties.get(VERSION_CATALOG_EXT) as MutableSet<VersionCatalog>
    set(value) {
        extraProperties[VERSION_CATALOG_EXT] = value
    }

internal val Settings.libs: VersionCatalog
    get() = allLibs.versionCatalog("libs")!!

internal fun Set<VersionCatalog>.versionCatalog(name: String) =
    find { versionCatalog -> versionCatalog.name == name }

internal fun Set<VersionCatalog>.resolveDependency(
    notation: String,
    directory: Directory,
    project: (name: String) -> Project = { name -> throw UnsupportedOperationException("Can't resolve project: '$name'") }
): Any = when {
    notation.startsWith("$") -> resolveRef(notation, VersionCatalog::library).notation
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

context(Project)
internal fun String.resolvePluginId() =
    if (startsWith("$")) project.settings.allLibs.resolveRef(removePrefix("$"), VersionCatalog::plugin).id
    else this

context(Project)
internal fun String.resolveVersion() =
    if (startsWith("$")) project.settings.allLibs.resolveRef(removePrefix("$"), VersionCatalog::versionOrNull)
    else this

private fun <T> Set<VersionCatalog>.resolveRef(
    ref: String,
    resolver: VersionCatalog.(name: String) -> T
): T {
    val versionCatalogName = ref
        .removePrefix("$")
        .substringBefore(".")
    val name = ref.substringAfter(".", "")

    return resolver(
        (versionCatalog(versionCatalogName)
            ?: error("Not found version catalog: $versionCatalogName")),
        name,
    )
}

private val String.asVersionCatalogAlias
    get() = replace(".", "-")

