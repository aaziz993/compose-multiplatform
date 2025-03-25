@file:Suppress("UnstableApiUsage")

package gradle.accessors

import org.gradle.api.Project
import org.gradle.api.initialization.Settings
import org.jetbrains.amper.gradle.getBindingMap
import org.jetbrains.kotlin.gradle.plugin.extraProperties
import org.tomlj.TomlParseResult
import org.tomlj.TomlTable

private const val VERSION_CATALOG = "version.catalog"

@Suppress("UNCHECKED_CAST")
internal val Settings.allLibs: MutableMap<String, TomlParseResult>
    get() = extraProperties.getBindingMap(VERSION_CATALOG)

internal val Settings.libs: TomlParseResult
    get() = allLibs["libs"]!!

private fun String.asCatalogAlias() = replace(".", "-")

internal fun TomlTable.version(alias: String) = getString(alias.asCatalogAlias())

internal fun TomlTable.plugin(alias: String): TomlTable = alias.asCatalogAlias().let { name ->
    getTable(name) ?: error("Not found plugin: $name")
}

internal fun TomlTable.library(alias: String): TomlTable = alias.asCatalogAlias().let { name ->
    getTable(name) ?: error("Not found library: $name")
}

internal val TomlTable.id
    get() = getString("id")!!

internal val TomlTable.group
    get() = getString("group") ?: getString("module")?.substringBefore(":")

internal val TomlTable.name
    get() = getString("name") ?: getString("module")?.substringAfter(":")

internal val TomlTable.module
    get() = getString("module") ?: "${getString("group")}:${getString("name")}"

context(TomlParseResult)
private val TomlTable.version: String?
    get() = get("version")?.let { version ->
        if (version is TomlTable) {
            return@let version.getString("ref")?.let(versions::version)
        }
        version.toString()
    }

internal fun TomlParseResult.libraryAsDependency(alias: String) =
    libraries.library(alias).let { library ->
        "${library.module}${library.version?.let { ":$it" }}"
    }

internal val TomlParseResult.versions
    get() = getTable("versions")!!

internal val TomlParseResult.libraries
    get() = getTable("libraries")!!

internal val TomlParseResult.plugins
    get() = getTable("plugins")!!

internal fun Map<String, TomlParseResult>.resolveLibrary(notation: String): String {
    val catalogName = notation
        .removePrefix("$")
        .substringBefore(".")
    val libraryName = notation
        .substringAfter(".")
    return this[catalogName]?.libraryAsDependency(libraryName)
        ?: error("Not found version catalog: $catalogName")
}

internal fun Map<String, TomlParseResult>.resolveVersion(version: String): String? {
    val catalogName = version
        .removePrefix("$")
        .substringBefore(".")
    val versionAlias = version
        .substringAfter(".")
    return this[catalogName]?.versions?.version(versionAlias)
}

context(Project)
internal fun String.resolveVersion() =
    if (startsWith("$")) project.settings.allLibs.resolveVersion(removePrefix("$"))
    else this

internal fun String.toVersionCatalogUrlPath(): String {
    val fileNamePart = substringAfter(":").replace(":", "-")
    return "${substringBeforeLast(":").replace("[.:]".toRegex(), "/")}/${
        substringAfterLast(":")
    }/$fileNamePart.toml"
}
