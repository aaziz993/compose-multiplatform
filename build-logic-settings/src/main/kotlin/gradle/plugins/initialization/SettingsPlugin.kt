@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package gradle.plugins.initialization

import gradle.accessors.catalog.allLibs
import gradle.accessors.catalog.libs
import gradle.accessors.catalog.toVersionCatalog
import gradle.accessors.projectProperties
import gradle.api.configureEach
import gradle.api.repositories.CacheRedirector
import gradle.plugins.develocity.DevelocityPlugin
import gradle.plugins.githooks.GitHooksPlugin
import gradle.plugins.project.ProjectPlugin
import gradle.plugins.project.ProjectProperties.Companion.load
import gradle.plugins.toolchainmanagement.ToolchainManagementPlugin
import java.io.File
import java.net.URI
import org.gradle.api.Plugin
import org.gradle.api.file.FileCollection
import org.gradle.api.initialization.Settings
import org.gradle.api.internal.artifacts.repositories.DefaultMavenArtifactRepository
import org.jetbrains.compose.internal.IDEA_IMPORT_TASK_NAME
import org.jetbrains.compose.internal.utils.currentTarget
import org.tomlj.Toml

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
                projectProperties = load()

                projectProperties.buildscript?.applyTo()

                projectProperties.pluginManagement.let { pluginManagement ->
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

                projectProperties.dependencyResolutionManagement?.let { dependencyResolutionManagement ->
                    dependencyResolutionManagement {
                        projectProperties.dependencyResolutionManagement?.repositories?.let { repositories ->
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
                                    allLibs += Toml.parse(libsFile.readText()).toVersionCatalog("libs")
                                }
                        }

                        dependencyResolutionManagement.versionCatalogs?.let { versionCatalogs ->
                            versionCatalogs {
                                allLibs += versionCatalogs
                                    .map { (name, dependency) -> name to dependency.resolve() }
                                    .map { (name, notation) ->
                                        create(name) {
                                            from(notation)
                                        }


                                        when (notation) {

                                            is String -> {
                                                val versionCatalogCacheFile = settings.layout.settingsDirectory.file("$VERSION_CATALOG_CACHE_DIR/$name.versions.toml").asFile

                                                if (versionCatalogCacheFile.exists())
                                                    Toml.parse(versionCatalogCacheFile.readText()).toVersionCatalog(name)
                                                else repositories.map { (it as DefaultMavenArtifactRepository).url.toString().removeSuffix("/") }
                                                    .firstNotNullOf { url ->
                                                        try {
                                                            Toml.parse(
                                                                URI("$url/$notation").toURL().readText()
                                                                    .also(versionCatalogCacheFile::writeText), // write to cache
                                                            ).toVersionCatalog(name)
                                                        }
                                                        catch (_: Throwable) {
                                                            null
                                                        }
                                                    }
                                            }

                                            is FileCollection -> Toml.parse(notation.files.single().readText()).toVersionCatalog(name)

                                            else -> throw IllegalArgumentException("Unknown version catalog notation: $notation")
                                        }
                                    }
                            }

                            // Load pre-defined compose version catalog
                            allLibs += Toml.parse(
                                settings.layout.settingsDirectory.file(COMPOSE_VERSION_CATALOG_FILE).asFile
                                    .readText()
                                    .replace("\$kotlin", libs.version("kotlin"))
                                    .replace("\$compose", libs.version("compose"))
                                    .replace("\$materialExtendedIcon", libs.version("materialExtendedIcon"))
                                    .replace("\$currentTargetId", currentTarget.id),
                            ).toVersionCatalog("compose")
                        }
                    }
                }

                CacheRedirector.applyTo()

                // Apply plugins after version catalogs are loaded in settings phase.
                settings.pluginManager.apply(DevelocityPlugin::class.java)
                settings.pluginManager.apply(ToolchainManagementPlugin::class.java)
                settings.pluginManager.apply(GitHooksPlugin::class.java)

                // Include projects.
                projectProperties.includes?.forEach(::include)

                // Include flat projects.
                projectProperties.includeFlats?.let(::includeFlat)

                // Include flat builds.
                projectProperties.includeBuilds?.forEach { (rootProject, configuration) ->
                    includeBuild(rootProject) {
                        configuration?.applyTo(this)
                    }
                }

                // Apply to configure all above included rojects.
                projectProperties.projects?.forEach { project ->
                    project.applyTo()
                }

                projectProperties.buildCache?.applyTo(buildCache)
            }

            target.gradle.projectsLoaded {
                // Apply project files
                with(rootProject) {
                    val projectFiles = (listOfNotNull(
                        target.projectProperties.licenseFile,
                        target.projectProperties.codeOfConductFile,
                        target.projectProperties.contributingFile,
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
                    ) + target.projectProperties.projectFiles.orEmpty()).flatMapIndexed { index, projectFile ->
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
