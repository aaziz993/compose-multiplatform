package gradle.api.initialization

import gradle.api.initialization.dsl.VERSION_CATALOG_DIR
import gradle.api.initialization.dsl.VersionCatalog
import gradle.api.repositories.CacheRedirector
import gradle.plugins.getOrPut
import java.io.File
import java.util.*
import kotlinx.serialization.decodeFromString
import net.peanuuutz.tomlkt.Toml
import klib.data.type.primitives.string.case.toScreamingSnakeCase
import org.danilopianini.gradle.git.hooks.GitHooksExtension
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.initialization.Settings
import org.gradle.api.initialization.dsl.VersionCatalogBuilder
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.plugin.extraProperties

public const val LOCAL_PROPERTIES_EXT: String = "local.properties.ext"

public const val VERSION_CATALOGS_EXT: String = "version.catalogs.ext"

public const val LIBS_VERSION_CATALOG_EXT: String = "libs"
public const val LIBS_VERSION_CATALOG_FILE: String = "$VERSION_CATALOG_DIR/$LIBS_VERSION_CATALOG_EXT.versions.toml"

@Suppress("UnstableApiUsage")
public val Settings.localProperties: Properties
    get() = extraProperties.getOrPut(LOCAL_PROPERTIES_EXT) {
        Properties().apply {
            layout.settingsDirectory.file("local.properties").asFile.takeIf(File::exists)
                ?.reader()
                ?.let(::load)
        }
    }

public val Settings.catalogs: MutableMap<String, VersionCatalog>
    get() = extraProperties.getOrPut(VERSION_CATALOGS_EXT, ::mutableMapOf)

public fun Settings.libs(name: String): VersionCatalog =
    settings.catalogs[name] ?: throw IllegalArgumentException("Unresolved version catalog '$name'")

@Suppress("UnstableApiUsage", "UNCHECKED_CAST")
public val Settings.libs: VersionCatalog
    get() = catalogs.getOrPut(LIBS_VERSION_CATALOG_EXT) {
        Toml.decodeFromString<VersionCatalog>(
            layout.settingsDirectory.file(LIBS_VERSION_CATALOG_FILE).asFile.also { file ->
                if (!file.exists())
                    error("Unresolved version catalog file '$LIBS_VERSION_CATALOG_FILE'")
            }.readText(),
        )
    }

@Suppress("UNCHECKED_CAST")
context(settings: Settings)
public fun VersionCatalogBuilder.fromLib(lib: MinimalExternalModuleDependency) {
    from(lib.toString())
    settings.catalogs[name] = VersionCatalog(lib)
}

public val Settings.gitHooks: GitHooksExtension
    get() = extensions.getByType<GitHooksExtension>()

public fun Settings.enableCacheRedirect(): Unit = CacheRedirector.applyTo(settings)

public fun Settings.execute(cmd: String): String = providers.exec {
    commandLine(cmd.split(" "))
}.standardOutput.asText.get().trim()

public fun Settings.gitCommitId(): String = execute("git rev-parse --verify HEAD")

public fun Settings.gitBranch(): String = execute("git rev-parse --abbrev-ref HEAD")

public fun Settings.gitStatus(): String = execute("git status --porcelain")

public fun Settings.sensitiveOrElse(key: String, defaultValue: () -> String): String =
    System.getenv(key.toScreamingSnakeCase())
        ?: localProperties.getProperty(key)
        ?: defaultValue()

public fun Settings.sensitive(key: String): String = sensitiveOrElse(key) {
    throw IllegalArgumentException("Unresolved sensitive variable $key")
}
