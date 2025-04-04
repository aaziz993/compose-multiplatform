@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package gradle.plugins.initialization

import gradle.api.VersionCatalog
import gradle.api.configureEach
import gradle.api.initialization.InitializationProperties
import gradle.api.initialization.initializationProperties
import gradle.api.repositories.CacheRedirector
import gradle.plugins.develocity.DevelocityPlugin
import gradle.plugins.githooks.GitHooksPlugin
import gradle.plugins.project.ProjectPlugin
import gradle.plugins.toolchainmanagement.ToolchainManagementPlugin
import java.io.File
import java.net.URI
import org.gradle.api.Plugin
import org.gradle.api.file.FileCollection
import org.gradle.api.initialization.Settings
import org.gradle.api.internal.artifacts.repositories.DefaultMavenArtifactRepository
import org.gradle.kotlin.dsl.apply
import org.jetbrains.compose.internal.IDEA_IMPORT_TASK_NAME
import org.jetbrains.compose.internal.utils.currentTarget

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
                InitializationProperties.load()

                initializationProperties.buildscript?.applyTo()

                initializationProperties.pluginManagement.let { pluginManagement ->
                    pluginManagement {
                        pluginManagement?.repositories?.let { repositories ->
                            repositories {
                                repositories.forEach { repository ->
                                    repository.applyTo(this)
                                }
                            }
                        }
                    }
                }

                initializationProperties.dependencyResolutionManagement?.let { dependencyResolutionManagement ->
                    dependencyResolutionManagement {
                        initializationProperties.dependencyResolutionManagement?.repositories?.let { repositories ->
                            repositories {
                                repositories.forEach { repository ->
                                    repository.applyTo(this)
                                }
                            }
                        }

                        allLibs = emptySet()

                        if (dependencyResolutionManagement.versionCatalogs.orEmpty().none { it.name == "libs" }) {
                            settings.layout.settingsDirectory.file("gradle/libs.versions.toml").asFile
                                .takeIf(File::exists)?.let { libsFile ->
                                    allLibs += VersionCatalog.parse("libs", libsFile.readText())
                                }
                        }

                        dependencyResolutionManagement.versionCatalogs?.let { versionCatalogs ->
                            versionCatalogs {
                                allLibs += versionCatalogs
                                    .map { (name, dependency) -> name to dependency.notation.notation }
                                    .map { (name, notation) ->
                                        create(name) {
                                            from(notation)
                                        }


                                        VersionCatalog.parse(
                                            name,
                                            when (notation) {
                                                is String -> {
                                                    val versionCatalogCacheFile =
                                                        settings.layout.settingsDirectory.file(
                                                            "$VERSION_CATALOG_CACHE_DIR/$name.versions.toml",
                                                        ).asFile

                                                    if (versionCatalogCacheFile.exists())
                                                        versionCatalogCacheFile.readText()
                                                    else repositories
                                                        .map {
                                                            (it as DefaultMavenArtifactRepository)
                                                                .url
                                                                .toString()
                                                                .removeSuffix("/")
                                                        }
                                                        .firstNotNullOf { url ->
                                                            URI("$url/$notation")
                                                                .toURL()
                                                                .readText()
                                                                // write to version catalog cache
                                                                .also(versionCatalogCacheFile::writeText)
                                                        }
                                                }

                                                is FileCollection -> notation.files.single().readText()

                                                else -> throw IllegalArgumentException("Unknown version catalog notation: $notation")
                                            },
                                        )
                                    }
                            }

                            // Load pre-defined compose version catalog
                            allLibs += VersionCatalog.parse(
                                "compose",
                                settings.layout.settingsDirectory.file(COMPOSE_VERSION_CATALOG_FILE).asFile
                                    .readText()
                                    .replace("\$kotlin", libs.version("kotlin"))
                                    .replace("\$compose", libs.version("compose"))
                                    .replace("\$materialExtendedIcon", libs.version("materialExtendedIcon"))
                                    .replace("\$currentTargetId", currentTarget.id),
                            )
                        }
                    }
                }

                CacheRedirector.applyTo()

                // Apply plugins after version catalogs are loaded in settings phase.
                pluginManager.apply(DevelocityPlugin::class.java)
                pluginManager.apply(ToolchainManagementPlugin::class.java)
                pluginManager.apply(GitHooksPlugin::class.java)

                initializationProperties.applies?.forEach { (from, plugin, to) ->
                    apply(from, plugin, to)
                }

                // Include projects.
                initializationProperties.includes?.forEach(::include)

                // Include flat projects.
                initializationProperties.includeFlats?.let(::includeFlat)

                // Include flat builds.
                initializationProperties.includeBuilds?.forEach { (rootProject, configuration) ->
                    includeBuild(rootProject) {
                        configuration?.applyTo(this)

                        dependencySubstitution {
                            substitute(module("com.squareup.wire:wire-gradle-plugin"))
                                .using(project(":wire-gradle-plugin"))
                            substitute(module("com.squareup.wire:wire-runtime"))
                                .using(project(":wire-runtime"))
                        }
                    }
                }

                // Apply to configure all above included rojects.
                initializationProperties.projects?.forEach { project ->
                    project.applyTo()
                }

                initializationProperties.buildCache?.applyTo(buildCache)
            }

            target.gradle.projectsLoaded {
                // Apply project files
                with(rootProject) {
                    val projectFiles = (listOfNotNull(
                        target.initializationProperties.licenseFile,
                        target.initializationProperties.codeOfConductFile,
                        target.initializationProperties.contributingFile,
                    ) + listOf(
//                        LicenseHeaderFile(
//                            "templates/LICENSE_HEADER_SLASHED",
//                            "licenses",
//                        ),
//                        ProjectFileImpl(
//                            "templates/LICENSE_HEADER_SHARPED",
//                            "licenses",
//                        ),
//                        ProjectFileImpl(
//                            "templates/LICENSE_HEADER_TAGGED",
//                            "licenses",
//                        ),
                    ) + target.initializationProperties.projectFiles.orEmpty()).flatMapIndexed { index, projectFile ->
                        projectFile.applyTo("projectFile$index")
                    }

                    //setup sync tasks execution during IDE import
                    tasks.configureEach { importTask ->
                        if (importTask.name == IDEA_IMPORT_TASK_NAME) {
                            importTask.dependsOn(*projectFiles.toTypedArray())
                        }
                    }
                }

                // at this point all projects have been created by settings.gradle.kts, but none were evaluated yet
                allprojects {
                    pluginManager.apply(ProjectPlugin::class.java)
                }
            }
        }
    }
}
