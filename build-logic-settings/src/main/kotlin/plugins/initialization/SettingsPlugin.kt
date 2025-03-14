@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package plugins.initialization

import gradle.accessors.allLibs
import gradle.accessors.exportExtras
import gradle.accessors.libs
import gradle.accessors.projectProperties
import gradle.accessors.toVersionCatalogUrlPath
import gradle.accessors.version
import gradle.accessors.versions
import gradle.api.repositories.CacheRedirector
import gradle.isUrl
import gradle.project.ProjectProperties.Companion.load
import java.net.URI
import org.gradle.api.Plugin
import org.gradle.api.file.FileCollection
import org.gradle.api.initialization.Settings
import org.gradle.api.internal.artifacts.repositories.DefaultMavenArtifactRepository
import org.gradle.kotlin.dsl.maven
import org.jetbrains.compose.internal.utils.currentTarget
import org.tomlj.Toml
import plugins.develocity.DevelocityPlugin
import plugins.githooks.GitHooksPlugin
import plugins.initialization.problemreporter.SLF4JProblemReporterContext
import plugins.project.ProjectPlugin
import plugins.toolchainmanagement.ToolchainManagementPlugin

private const val VERSION_CATALOG_CACHE_DIR = "build-logic-settings/gradle"

private const val COMPOSE_VERSION_CATALOG_FILE = "build-logic-settings/gradle/compose.versions.template.toml"

/**
 * Gradle setting plugin, that is responsible for:
 * 1. Initializing gradle projects, based on the Amper model.
 * 2. Applying kotlin or KMP plugins.
 * 3. Associate gradle projects with modules.
 */
public class SettingsPlugin : Plugin<Settings> {

    @Suppress("UnstableApiUsage")
    override fun apply(target: Settings) {
        with(SLF4JProblemReporterContext()) {
            with(target) {
                // Load and apply project.yaml to settings.gradle.kts.
                projectProperties = layout.settingsDirectory.load()

                exportExtras()

                projectProperties.pluginManagement.let { pluginManagement ->
                    pluginManagement {
                        repositories {
                            mavenCentral()
                            google()
                            gradlePluginPortal()

                            pluginManagement?.repositories?.forEach { repository -> maven(repository) }
                        }
                    }
                }

                projectProperties.dependencyResolutionManagement?.let { dependencyResolutionManagement ->
                    dependencyResolutionManagement {
                        repositories {
                            projectProperties.dependencyResolutionManagement?.repositories?.let { repositories ->
                                repositories.forEach { repository ->
                                    repository.applyTo(this)
                                }
                            }
                        }

                        val libsFile = settings.layout.settingsDirectory.file("gradle/libs.versions.toml").asFile
                        if (dependencyResolutionManagement.versionCatalogs?.none { it.name == "libs" } != false && libsFile.exists())
                            allLibs += "libs" to Toml.parse(libsFile.readText())


                        dependencyResolutionManagement.versionCatalogs?.let { versionCatalogs ->
                            versionCatalogs {
                                versionCatalogs.forEach { (catalogName, dependency) ->
                                    var notation = dependency.resolve()

                                    create(catalogName) {
                                        from(notation)
                                    }


                                    if (notation is FileCollection) {
                                        allLibs[catalogName] = Toml.parse(notation.files.single().readText())
                                    }
                                    else {
                                        notation as String

                                        val cacheFile = settings.layout.settingsDirectory.file("$VERSION_CATALOG_CACHE_DIR/$catalogName.versions.toml").asFile

                                        if (cacheFile.exists()) {
                                            allLibs[catalogName] = Toml.parse(cacheFile.readText())
                                            return@forEach
                                        }

                                        if (!notation.isUrl) {
                                            notation = notation.toVersionCatalogUrlPath()
                                        }

                                        allLibs[catalogName] = repositories.map { (it as DefaultMavenArtifactRepository).url.toString().removeSuffix("/") }
                                            .firstNotNullOf { url ->
                                                try {
                                                    Toml.parse(
                                                        URI(
                                                            "$url/$notation",
                                                        ).toURL()
                                                            .readText()
                                                            .also(cacheFile::writeText), // write to cache
                                                    )
                                                }
                                                catch (_: Throwable) {
                                                    null
                                                }
                                            }
                                    }
                                }
                            }
                        }

                        // Load pre-defined compose version catalog
                        allLibs += "compose" to Toml.parse(
                            settings.layout.settingsDirectory.file(COMPOSE_VERSION_CATALOG_FILE).asFile
                                .readText()
                                .replace("\$kotlin", libs.versions.version("kotlin")!!)
                                .replace("\$compose", libs.versions.version("compose")!!)
                                .replace("\$materialExtendedIcon", libs.versions.version("materialExtendedIcon")!!)
                                .replace("\$currentTargetId", currentTarget.id),
                        )
                    }
                }

                projectProperties.buildscript?.applyTo()

                CacheRedirector.applyTo()

                // Apply plugins after version catalogs are loaded in settings phase.
                settings.plugins.apply(DevelocityPlugin::class.java)
                settings.plugins.apply(ToolchainManagementPlugin::class.java)
                settings.plugins.apply(GitHooksPlugin::class.java)

                // Include subprojects.
                projectProperties.includes?.forEach(::include)

                // Configure subprojects.
                projectProperties.projects?.forEach { project ->
                    project.applyTo()
                }

                projectProperties.buildCache?.applyTo(buildCache)
            }

            target.gradle.projectsLoaded {
                // at this point all projects have been created by settings.gradle.kts, but none were evaluated yet
                allprojects {
                    plugins.apply(ProjectPlugin::class.java)
                }
            }
        }
    }
}
