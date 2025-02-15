package gradle

import org.gradle.api.Project
import org.gradle.api.initialization.Settings
import org.tomlj.Toml
import org.tomlj.TomlParseResult
import org.tomlj.TomlTable

/**
 * Accessor to make gradle.version catalog available in build-logic.
 * See: https://github.com/gradle/gradle/issues/15383#issuecomment-779893192
 */
//internal val Project.libs: LibrariesForLibs
//    get() = rootProject.the()

@Suppress("UnstableApiUsage")
internal val Settings.libs: TomlParseResult
    get() = Toml.parse(layout.rootDirectory.file("gradle/libs.versions.toml").asFile.readText())

internal fun TomlTable.version(alias: String) =
    getString(alias)!!

internal fun TomlTable.plugin(alias: String): TomlTable = getTable(alias)!!

internal val TomlTable.id
    get() = getString("id")!!

internal val TomlTable.group
    get() = getString("group") ?: getString("module")?.substringBefore(":")

internal val TomlTable.name
    get() = getString("name") ?: getString("module")?.substringAfter(":")

internal val TomlTable.module
    get() = getString("module") ?: "${getString("group")}:${getString("name")}"

context(TomlParseResult)
internal val TomlTable.version: String?
    get() = getTable("version")?.getString("ref")?.let(versions::version)

context(TomlParseResult)
internal val TomlTable.pluginAsDependency: String
    get() = "$id:$id.gradle.plugin:${version!!}"

internal fun TomlTable.pluginAsDependency(version: String): String = "$id:$id.gradle.plugin:$version"

context(TomlParseResult)
internal val TomlTable.libraryAsDependency
    get() = "$module${version?.let { ":$it" }}"

internal val TomlParseResult.versions
    get() = getTable("versions")!!

internal val TomlParseResult.libraries
    get() = getTable("libraries")!!

internal val TomlParseResult.plugins
    get() = getTable("plugins")!!



