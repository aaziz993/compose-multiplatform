@file:Suppress("UnstableApiUsage")

package gradle

import org.gradle.api.initialization.Settings
import org.jetbrains.kotlin.gradle.plugin.extraProperties
import org.tomlj.TomlParseResult
import org.tomlj.TomlTable

private const val VERSION_CATALOG = "version.catalog"

@Suppress("UNCHECKED_CAST")
internal var Settings.allLibs: Map<String, TomlParseResult>
    get() = extraProperties[VERSION_CATALOG] as Map<String, TomlParseResult>
    set(value) {
        extraProperties[VERSION_CATALOG] = value
    }

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
    get() = getTable("version")?.getString("ref")?.let(versions::version)

internal fun TomlParseResult.pluginAsDependency(alias: String): String =
    plugins.plugin(alias).let { plugin ->
        "${plugin.id}:${plugin.id}.gradle.plugin:${plugin.version!!}"
    }

internal fun TomlTable.pluginAsDependency(version: String): String = "$id:$id.gradle.plugin:$version"

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
