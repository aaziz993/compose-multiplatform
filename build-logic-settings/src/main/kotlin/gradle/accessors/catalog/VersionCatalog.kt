package gradle.accessors.catalog

import gradle.accessors.settings
import gradle.isPath
import gradle.isUrl
import gradle.isValidUrl
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.initialization.Settings
import org.jetbrains.amper.gradle.getBindingMap
import org.jetbrains.kotlin.gradle.plugin.extraProperties

@Serializable
internal data class VersionCatalog(
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

    fun version(alias: String) = versions[alias.asCatalogAlias()]

    fun library(alias: String): Library = alias.asCatalogAlias().let { name ->
        libraries[name] ?: error("Not found library: $name")
    }

    fun plugin(alias: String): Plugin = alias.asCatalogAlias().let { name ->
        plugins[name] ?: error("Not found plugin: $name")
    }

    private fun String.asCatalogAlias() = replace(".", "-")
}

private const val VERSION_CATALOG = "version.catalog"

@Suppress("UNCHECKED_CAST")
internal val Settings.allLibs: MutableMap<String, VersionCatalog>
    get() = extraProperties.getBindingMap(VERSION_CATALOG)

internal val Settings.libs: VersionCatalog
    get() = allLibs["libs"]!!

internal fun Map<String, VersionCatalog>.resolveLibrary(
    directory: Directory,
    notation: String,
    fromNotation: (String) -> Any = { it }
): Any = when {
    notation.startsWith("$") -> fromNotation(resolveLibraryRef(notation).notation)
    notation.isPath -> directory.file(notation)
    notation.isValidUrl -> notation.asVersionCatalogUrl
    else -> fromNotation(notation)
}

private fun Map<String, VersionCatalog>.resolveLibraryRef(notation: String): Library {
    val catalogName = notation
        .removePrefix("$")
        .substringBefore(".")
    val libraryName = notation
        .substringAfter(".", "")
    return this[catalogName]?.library(libraryName)
        ?: error("Not found version catalog: $catalogName")
}

private val String.asVersionCatalogUrl: String
    get() {
        val fileNamePart = substringAfter(":", "").replace(":", "-")

        return "${substringBeforeLast(":").replace("[.:]".toRegex(), "/")}/${
            substringAfterLast(":", "")
        }/$fileNamePart.toml"
    }

context(Project)
internal fun String.resolveVersion() =
    if (startsWith("$")) project.settings.allLibs.resolveVersionRef(removePrefix("$"))
    else this

private fun Map<String, VersionCatalog>.resolveVersionRef(version: String): String? {
    val catalogName = version
        .removePrefix("$")
        .substringBefore(".")
    val versionAlias = version
        .substringAfter(".", "")
    return this[catalogName]?.version(versionAlias)
}
