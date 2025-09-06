package gradle.api.initialization

import gradle.api.initialization.dsl.VersionCatalog
import gradle.api.initialization.dsl.toCatalogUrl
import gradle.api.repositories.CacheRedirector
import gradle.plugins.getOrPut
import kotlinx.serialization.decodeFromString
import net.peanuuutz.tomlkt.Toml
import org.danilopianini.gradle.git.hooks.GitHooksExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.artifacts.repositories.IvyArtifactRepository
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.initialization.Settings
import org.gradle.api.initialization.dsl.VersionCatalogBuilder
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.plugin.extraProperties
import java.io.File
import java.net.URI
import java.util.*

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

context(settings: Settings)
public fun VersionCatalogBuilder.fromLibs(alias: String) {
    settings.extraProperties[name] = settings.dependencyResolutionManagement.repositories.firstNotNullOfOrNull { repo ->
        URI(
            "${
                when (repo) {
                    is MavenArtifactRepository -> repo.url.toString()
                    is IvyArtifactRepository -> repo.url.toString()
                    else -> null // flatDir etc. don’t have a URL
                }
            }${settings.libs(alias).toCatalogUrl()}"
        ).toURL().readText()
    }?.let { text ->
        Toml.decodeFromString<VersionCatalog>(text)
    } ?: error("Couldn't find version catalog ''")
}

public fun Settings.allLibs(name: String): VersionCatalog =
    (extraProperties[name] ?: throw IllegalArgumentException("Unresolved version catalog '$name'")) as VersionCatalog

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

