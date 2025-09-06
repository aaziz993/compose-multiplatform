package gradle.api.initialization

import gradle.api.initialization.dsl.VersionCatalog
import gradle.api.project.settings
import gradle.api.repositories.CacheRedirector
import gradle.plugins.getOrPut
import java.io.File
import kotlinx.serialization.decodeFromString
import net.peanuuutz.tomlkt.Toml
import org.danilopianini.gradle.git.hooks.GitHooksExtension
import org.gradle.api.Project
import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.plugin.extraProperties
import java.util.Properties

public const val LIBS_VERSION_CATALOG_EXT: String = "libs.versions.catalog.ext"

public const val LOCAL_PROPERTIES_EXT: String = "local.properties.ext"

@Suppress("UnstableApiUsage")
public val Settings.localProperties: Properties
    get() = extraProperties.getOrPut(LOCAL_PROPERTIES_EXT) {
        Properties().apply {
            layout.settingsDirectory.file("local.properties").asFile.takeIf(File::exists)
                ?.reader()
                ?.let(::load)
        }
    }

@Suppress("UnstableApiUsage")
public val Settings.libs: VersionCatalog
    get() = extraProperties.getOrPut(LIBS_VERSION_CATALOG_EXT) {
        Toml.Default.decodeFromString(
            layout.settingsDirectory.file("gradle/libs.versions.toml").asFile.readText(),
        )
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

public fun Settings.sensitive(key: String): String = System.getenv(key) ?: localProperties.getProperty(key)

