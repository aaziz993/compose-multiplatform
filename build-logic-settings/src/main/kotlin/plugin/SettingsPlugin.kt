@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package plugin

import gradle.amper.chooseComposeVersion
import gradle.amper.setupDynamicClasspath
import gradle.libs
import gradle.plugin
import gradle.pluginAsDependency
import gradle.plugins
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.initialization.Settings
import org.gradle.api.invocation.Gradle
import org.jetbrains.amper.core.Result
import org.jetbrains.amper.core.UsedVersions
import org.jetbrains.amper.core.get
import org.jetbrains.amper.frontend.AmperModule
import org.jetbrains.amper.frontend.AmperModuleFileSource
import org.jetbrains.amper.frontend.Model
import org.jetbrains.amper.frontend.ModelInit
import org.jetbrains.amper.gradle.*
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper
import org.jetbrains.kotlin.gradle.plugin.extraProperties
import plugin.project.BindingProjectPlugin
import kotlin.io.path.absolutePathString
import kotlin.io.path.nameWithoutExtension


/**
 * Gradle setting plugin, that is responsible for:
 * 1. Initializing gradle projects, based on the Amper model.
 * 2. Applying kotlin or KMP plugins.
 * 3. Associate gradle projects with modules.
 */
// This is registered via FQN from the resources in org.jetbrains.amper.settings.plugin.properties
public class SettingsPlugin : Plugin<Settings> {

    override fun apply(settings: Settings) {
        with(SLF4JProblemReporterContext()) {

            // the class loader is different within projectsLoaded, and we need this one to load the ModelInit service
            val gradleClassLoader = Thread.currentThread().contextClassLoader
            settings.gradle.projectsLoaded {
                // at this point all projects have been created by settings.gradle.kts, but none were evaluated yet
                val projects = settings.gradle.rootProject.allprojects

                val modelResult = ModelInit.getGradleAmperModel(
                    rootProjectDir = settings.rootDir.toPath().toAbsolutePath(),
                    subprojectDirs = projects.map { it.projectDir.toPath().toAbsolutePath() },
                    loader = gradleClassLoader,
                )
                if (modelResult is Result.Failure<Model> || problemReporter.hasFatal) {
                    throw GradleException(problemReporter.getGradleError())
                }


                val model = modelResult.get()

                settings.gradle.knownModel = model
                setGradleProjectsToAmperModuleMappings(projects, model.modules, settings.gradle)

                settings.setupPluginsClasspath(model)

                projects.forEach { project ->
                    if (project.amperModule != null) {
                        configureProjectForAmper(project)
                    } else if (project === project.rootProject) {
                        // Even if the root project doesn't have a module.yaml file (and thus is not an Amper project),
                        // subprojects using Kotlin/Native add the :commonizeNativeDistribution task to the root.
                        // The IDE runs it, as well as native subproject builds.
                        // Therefore, it needs mavenCentral to resolve kotlin-klib-commonizer-embeddable.
                        project.repositories.mavenCentral()
                    }
                }
            }
        }
    }

    private fun setGradleProjectsToAmperModuleMappings(
        projects: Set<Project>,
        modules: List<AmperModule>,
        gradle: Gradle,
    ) {
        val amperModulesByDir = modules
            .filter { it.hasAmperConfigFile() }
            .associateBy { it.moduleDir.absolutePathString() }

        projects.forEach { project ->
            val module = amperModulesByDir[project.projectDir.absolutePath] ?: return@forEach
            project.amperModule = AmperModuleWrapper(module)
            gradle.moduleFilePathToProjectPath[module.moduleDir] = project.path
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun Settings.setupPluginsClasspath(model: Model) {


        // We don't need to use the dynamic plugin mechanism if the user wants the embedded Compose version (because
        // it's already on the classpath). Using dynamic plugins relies on unreliable internal Gradle APIs, which are
        // absent in (or incompatible with) recent Gradle versions, so we only use this if absolutely necessary.
        var composePlugin: String? = null

        val chosenComposeVersion = chooseComposeVersion(model)

        if (chosenComposeVersion != null && chosenComposeVersion != UsedVersions.composeVersion) {
            composePlugin = libs.plugins.plugin("compose-multiplatform").pluginAsDependency(chosenComposeVersion)
        }

        with(libs) {
            setupDynamicClasspath(
                *listOfNotNull(
                    composePlugin,
                    libs.plugins.plugin("doctor").pluginAsDependency
                ).toTypedArray()
            ) {
                addDefaultAmperRepositoriesForDependencies()
            }
        }
    }

    private fun configureProjectForAmper(project: Project) {

        // /!\ This overrides any user configuration from settings.gradle.kts
        // This is only done in modules with Amper's module.yaml config to avoid issues
        project.repositories.addDefaultAmperRepositoriesForDependencies()

        // Disable warning about Default Kotlin Hierarchy.
        project.extraProperties.set("kotlin.mpp.applyDefaultHierarchyTemplate", "false")

        // Apply Kotlin plugins.
        project.plugins.apply(KotlinMultiplatformPluginWrapper::class.java)

        project.plugins.apply(BindingProjectPlugin::class.java)

        project.afterEvaluate {
            // W/A for XML factories mess within apple plugin classpath.
            val hasAndroidPlugin = plugins.hasPlugin("com.android.application") ||
                    plugins.hasPlugin("com.android.library")
            if (hasAndroidPlugin) {
                adjustXmlFactories()
            }
        }
    }
}

private fun AmperModule.hasAmperConfigFile() =
    (source as? AmperModuleFileSource)?.buildFile?.nameWithoutExtension == "module"

private fun RepositoryHandler.addDefaultAmperRepositoriesForDependencies() {
    mavenCentral()
    // For the Android plugin and dependencies
    google()
    // For other Gradle plugins
    gradlePluginPortal()
    // For dev versions of kotlin
    maven { setUrl("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev") }
    // For dev versions of compose plugin and dependencies
    maven { setUrl("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
    // Sonatype OSS Snapshot Repository
    maven { setUrl("https://oss.sonatype.org/content/repositories/snapshots") }

    // Space Packages releases
    maven { setUrl("https://maven.pkg.jetbrains.space/aaziz93/p/aaziz-93/maven") }
    // GitHub Packages
    maven { setUrl("https://maven.pkg.github.com/aaziz993") }
}
