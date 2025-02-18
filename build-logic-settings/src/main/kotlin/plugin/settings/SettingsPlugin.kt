@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package plugin.settings

import gradle.amperModuleExtraProperties
import gradle.amperProjectExtraProperties
import gradle.decodeFromAny
import gradle.deepMerge
import gradle.encodeToAny
import gradle.libs
import gradle.plugin
import gradle.pluginAsDependency
import gradle.plugins
import gradle.setupDynamicClasspath
import gradle.version
import gradle.versions
import kotlin.io.path.absolutePathString
import kotlin.io.path.nameWithoutExtension
import kotlin.io.path.readText
import kotlinx.serialization.json.Json
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.initialization.Settings
import org.gradle.api.invocation.Gradle
import org.gradle.kotlin.dsl.maven
import org.jetbrains.amper.core.Result
import org.jetbrains.amper.core.UsedVersions
import org.jetbrains.amper.core.get
import org.jetbrains.amper.frontend.AmperModule
import org.jetbrains.amper.frontend.AmperModuleFileSource
import org.jetbrains.amper.frontend.Model
import org.jetbrains.amper.frontend.ModelInit
import org.jetbrains.amper.gradle.AmperModuleWrapper
import org.jetbrains.amper.gradle.SLF4JProblemReporterContext
import org.jetbrains.amper.gradle.adjustXmlFactories
import org.jetbrains.amper.gradle.amperModule
import org.jetbrains.amper.gradle.knownModel
import org.jetbrains.amper.gradle.moduleDir
import org.jetbrains.amper.gradle.moduleFilePathToProjectPath
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper
import org.jetbrains.kotlin.gradle.plugin.extraProperties
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml
import plugin.project.BindingProjectPlugin
import plugin.project.gradle.develocity.DevelocityPluginPart
import plugin.project.gradle.githooks.GitHooksluginPart
import plugin.project.gradle.toolchainmanagement.ToolchainManagementPluginPart
import plugin.project.model.Alias
import plugin.project.model.ModuleProperties
import plugin.project.web.node.configureNodeJsRootExtension
import plugin.project.web.npm.configureNpmExtension
import plugin.project.web.yarn.configureYarnRootExtension
import plugin.settings.model.ProjectProperties

/**
 * Gradle setting plugin, that is responsible for:
 * 1. Initializing gradle projects, based on the Amper model.
 * 2. Applying kotlin or KMP plugins.
 * 3. Associate gradle projects with modules.
 */
// This is registered via FQN from the resources in org.jetbrains.amper.settings.plugin.properties
public class SettingsPlugin : Plugin<Settings> {

    private val json = Json { ignoreUnknownKeys = true }

    private val yaml = Yaml()

    private val logYaml = Yaml(
        DumperOptions().apply {
            isPrettyFlow = true
        },
    )

    override fun apply(settings: Settings) {
        with(SLF4JProblemReporterContext()) {
            // Setup  settings.gradle.kts from project.yaml.
            settings.setupAmperProject()

            // the class loader is different within projectsLoaded, and we need this one to load the ModelInit service
            val gradleClassLoader = Thread.currentThread().contextClassLoader
            settings.gradle.projectsLoaded {
                // at this point all projects have been created by settings.gradle.kts, but none were evaluated yet
                val projects = settings.gradle.rootProject.allprojects

                settings.setupAmperModel(gradleClassLoader)

                projects.forEach { project ->
                    if (project.amperModule != null) {
                        configureProjectForAmper(project)
                    }
                    else if (project === project.rootProject) {
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

    @Suppress("UnstableApiUsage")
    private fun Settings.setupAmperProject() {
        setGradleExtraPropertiesToAmperProjectMappings()

        amperProjectExtraProperties.pluginManagement.let { pluginManagement ->
            pluginManagement {
                repositories {
                    mavenCentral()
                    google()
                    gradlePluginPortal()

                    pluginManagement?.repositories?.forEach { repository -> maven(repository) }
                }
            }
        }

        // Apply plugins.
        DevelocityPluginPart(this)
        ToolchainManagementPluginPart(this)
        GitHooksluginPart(this)

        amperProjectExtraProperties.dependencyResolutionManagement?.let { dependencyResolutionManagement ->
            dependencyResolutionManagement {
                repositories {
                    addDefaultAmperRepositoriesForDependencies()
                    dependencyResolutionManagement.repositories?.let { repositories ->
                        repositories.forEach { repository ->
                            maven(repository)
                        }
                    }
                }

                dependencyResolutionManagement.versionCatalogs?.let { versionCatalogs ->
                    versionCatalogs {
                        versionCatalogs.forEach { (name, files, dependency) ->
                            create(name).apply {
                                files?.let { layout.rootDirectory.files(*it.toTypedArray()) }
                                dependency?.let(::from)
                            }
                        }
                    }
                }
            }
        }

        amperProjectExtraProperties.modules?.forEach(::include)
    }

    private fun Settings.setGradleExtraPropertiesToAmperProjectMappings() {
        val projectSettings = yaml.load<Map<String, *>>(rootDir.resolve("project.yaml").readText())

        amperProjectExtraProperties = json
            .decodeFromAny<ProjectProperties>(projectSettings).also {
                println("Apply project.yaml to project '${rootProject.name.uppercase()}':")
                println(logYaml.dump(Json.Default.encodeToAny(it)))
            }
    }

    context(SLF4JProblemReporterContext)
    private fun Settings.setupAmperModel(loader: ClassLoader = Thread.currentThread().contextClassLoader) {
        val projects = settings.gradle.rootProject.allprojects

        val modelResult = ModelInit.getGradleAmperModel(
            rootProjectDir = rootDir.toPath().toAbsolutePath(),
            subprojectDirs = projects.map { it.projectDir.toPath().toAbsolutePath() },
            loader = loader,
        )

        if (modelResult is Result.Failure<Model> || problemReporter.hasFatal) {
            throw GradleException(problemReporter.getGradleError())
        }

        val model = modelResult.get()

        settings.gradle.knownModel = model
        setGradleProjectsToAmperModuleMappings(projects, model.modules, settings.gradle)
        setGradleExtraPropertiesToAmperModuleMappings(projects, gradle)

        settings.setupPluginsClasspath(model)
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
    private fun setGradleExtraPropertiesToAmperModuleMappings(
        projects: Set<Project>,
        gradle: Gradle,
    ) = projects.filterNot { it.amperModule == null }.forEach { project ->
        val module = project.amperModule!!

        fun tryTransform(settings: MutableMap<String, Any?>) {
            settings["aliases"] = (settings["aliases"] as List<Map<String, List<String>>>?)?.map { alias ->
                Alias(alias.keys.single(), alias.values.single())
            }.orEmpty()
        }

        val moduleSettings = yaml.load<MutableMap<String, *>>(
            module.moduleDir.resolve("module.yaml").readText(),
        ).toMutableMap().also(::tryTransform)

        val templates = (moduleSettings["apply"] as List<String>?)?.associateWith { template ->
            yaml.load<MutableMap<String, *>>(
                module.moduleDir.resolve(template).readText(),
            ).toMutableMap().also(::tryTransform)
        }.orEmpty()

        project.amperModuleExtraProperties = json.decodeFromAny<ModuleProperties>(
            templates.values.fold(emptyMap<String, Any?>()) { acc, map -> acc deepMerge map }
                deepMerge moduleSettings,
        ).apply {
            println("Apply module.yaml to '${module.userReadableName.uppercase()}':")
            println(logYaml.dump(Json.Default.encodeToAny(this)))
            this.templates = json.decodeFromAny(templates)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun Settings.setupPluginsClasspath(model: Model) {

        // We don't need to use the dynamic plugin mechanism if the user wants the embedded Compose version (because
        // it's already on the classpath). Using dynamic plugins relies on unreliable internal Gradle APIs, which are
        // absent in (or incompatible with) recent Gradle versions, so we only use this if absolutely necessary.
        var composePlugin: String? = null

        val composeVersion = libs.versions.version("compose-multiplatform")

        if (composeVersion != null && composeVersion != UsedVersions.composeVersion) {
            composePlugin = libs.plugins.plugin("compose-multiplatform").pluginAsDependency(composeVersion)
        }

        with(libs) {
            setupDynamicClasspath(
                *listOfNotNull(
                    composePlugin,
                ).toTypedArray(),
            ) {
                addDefaultAmperRepositoriesForDependencies()
            }
        }
    }

    private fun configureProjectForAmper(project: Project) {
        // Disable warning about Default Kotlin Hierarchy.
        project.extraProperties.set("kotlin.mpp.applyDefaultHierarchyTemplate", "false")

        // Apply Kotlin plugins.
        project.plugins.apply(KotlinMultiplatformPluginWrapper::class.java)

        project.plugins.apply(BindingProjectPlugin::class.java)



        project.afterEvaluate {
//            project.configureWeb()
            // W/A for XML factories mess within apple plugin classpath.
            val hasAndroidPlugin = plugins.hasPlugin("com.android.application") ||
                plugins.hasPlugin("com.android.library")
            if (hasAndroidPlugin) {
                adjustXmlFactories()
            }
        }
    }
}

private fun Project.configureWeb() {
    configureNodeJsRootExtension()
    configureNpmExtension()
    configureYarnRootExtension()
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
    // For compose experimental builds
    maven { setUrl("https://packages.jetbrains.team/maven/p/firework/dev") }
    // Sonatype OSS Snapshot Repository
    maven { setUrl("https://oss.sonatype.org/content/repositories/snapshots") }

    // Space Packages releases
    maven { setUrl("https://maven.pkg.jetbrains.space/aaziz93/p/aaziz-93/maven") }
    // GitHub Packages
    maven { setUrl("https://maven.pkg.github.com/aaziz993") }
}
