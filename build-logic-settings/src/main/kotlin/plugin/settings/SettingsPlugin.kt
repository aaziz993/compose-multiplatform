@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package plugin.settings

import gradle.moduleProperties
import gradle.projectProperties
import gradle.decodeFromAny
import gradle.deepMerge
import gradle.encodeToAny
import gradle.libs
import gradle.plugin
import gradle.pluginAsDependency
import gradle.plugins
import gradle.setupDynamicClasspath
import gradle.trySetSystemProperty
import javax.xml.stream.XMLEventFactory
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLOutputFactory
import kotlinx.serialization.json.Json
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.maven
import org.jetbrains.amper.gradle.SLF4JProblemReporterContext
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.nodes.Tag
import org.yaml.snakeyaml.representer.Represent
import org.yaml.snakeyaml.representer.Representer
import plugin.project.BindingProjectPlugin
import plugin.project.gradle.develocity.DevelocityPluginPart
import plugin.project.gradle.githooks.GitHooksluginPart
import plugin.project.gradle.toolchainmanagement.ToolchainManagementPluginPart
import plugin.project.model.Alias
import plugin.project.model.ModuleProperties
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
        object : Representer(DumperOptions()) {
            init {
                nullRepresenter = Represent { representScalar(Tag.NULL, "null") }
            }
        },
        DumperOptions(),
    )

    override fun apply(target: Settings) {
        with(SLF4JProblemReporterContext()) {

            // Setup  settings.gradle.kts from project.yaml.
            target.setupProject()

            target.gradle.projectsLoaded {
                target.setupPluginsClasspath()

                // at this point all projects have been created by settings.gradle.kts, but none were evaluated yet
                target.gradle.rootProject.allprojects {
                    configureProject()
                }

                target.gradle.rootProject.repositories.mavenCentral()
            }
        }
    }

    @Suppress("UnstableApiUsage")
    private fun Settings.setupProject() {
        loadProjectProperties()

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

        // Apply plugins.
        plugins.apply(DevelocityPluginPart::class.java)
        plugins.apply(ToolchainManagementPluginPart::class.java)
        plugins.apply(GitHooksluginPart::class.java)

        projectProperties.dependencyResolutionManagement?.let { dependencyResolutionManagement ->
            dependencyResolutionManagement {
                repositories {
                    addDefaultAmperRepositoriesForDependencies()
                }

                dependencyResolutionManagement.versionCatalogs?.let { versionCatalogs ->
                    versionCatalogs {
                        versionCatalogs.forEach { (name, dependency) ->
                            create(name) {
                                from(
                                    dependency.toDependencyNotation(),
                                )
                            }
                        }
                    }
                }
            }
        }

        projectProperties.modules?.forEach(::include)
    }

    private fun Settings.loadProjectProperties() {
        val projectSettings = yaml.load<Map<String, *>>(rootDir.resolve("project.yaml").readText())

        projectProperties = json
            .decodeFromAny<ProjectProperties>(projectSettings).also {
                println("Apply project.yaml to project '${rootProject.name.uppercase()}':")
                println(logYaml.dump(Json.Default.encodeToAny(it)))
            }
    }

    @Suppress("UNCHECKED_CAST")
    private fun Settings.setupPluginsClasspath() {
        with(libs) {
            setupDynamicClasspath(
                libs.plugins.plugin("kotlin-multiplatform").pluginAsDependency,
                libs.plugins.plugin("android").pluginAsDependency,
                libs.plugins.plugin("android-library").pluginAsDependency,
                libs.plugins.plugin("android-application").pluginAsDependency,
                libs.plugins.plugin("compose-multiplatform").pluginAsDependency,
            ) {
                addDefaultAmperRepositoriesForDependencies()
            }
        }
    }

    private fun Project.configureProject() {
        loadModuleProperties()


        plugins.apply(BindingProjectPlugin::class.java)

        afterEvaluate {
            // W/A for XML factories mess within apple plugin classpath.
            val hasAndroidPlugin = plugins.hasPlugin("com.android.application") ||
                plugins.hasPlugin("com.android.library")
            if (hasAndroidPlugin) {
                adjustXmlFactories()
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun Project.loadModuleProperties() {

        if (!project.file("module.yaml").exists()) {
            moduleProperties = ModuleProperties()
            return
        }

        fun tryTransform(config: MutableMap<String, Any?>) {
            config["aliases"] = (config["aliases"] as List<Map<String, List<String>>>?)?.map { alias ->
                Alias(alias.keys.single(), alias.values.single())
            }.orEmpty()

            config["dependencies"] = config.filterKeys { it.startsWith("dependencies") }.map { (key, notations) ->
                mapOf(
                    "sourceSetName" to key.substringAfter("@", KotlinSourceSet.COMMON_MAIN_SOURCE_SET_NAME),
                    "dependencyNotations" to notations,
                )
            }

            config["test-dependencies"] = config.filterKeys { it.startsWith("test-dependencies") }.map { (key, notations) ->
                mapOf(
                    "sourceSetName" to key.substringAfter("@", KotlinSourceSet.COMMON_TEST_SOURCE_SET_NAME),
                    "dependencyNotations" to notations,
                )
            }
        }

        val moduleSettings = yaml.load<MutableMap<String, *>>(
            project.projectDir.resolve("module.yaml").readText(),
        ).toMutableMap().also(::tryTransform)

        val templates = (moduleSettings["apply"] as List<String>?)?.map { template ->
            yaml.load<MutableMap<String, *>>(
                project.projectDir.resolve(template).readText(),
            ).toMutableMap().also(::tryTransform)
        }.orEmpty()

        project.moduleProperties = json.decodeFromAny<ModuleProperties>(
            templates.fold(emptyMap<String, Any?>()) { acc, map -> acc deepMerge map }
                deepMerge moduleSettings,
        ).apply {
            println("Apply module.yaml to '${project.name.uppercase()}':")
            println(logYaml.dump(Json.Default.encodeToAny(this)))
        }
    }

    /**
     * W/A for service loading conflict between apple plugin
     * and android plugin.
     */
    private fun adjustXmlFactories() {
        trySetSystemProperty(
            XMLInputFactory::class.qualifiedName!!,
            "com.sun.xml.internal.stream.XMLInputFactoryImpl",
        )
        trySetSystemProperty(
            XMLOutputFactory::class.qualifiedName!!,
            "com.sun.xml.internal.stream.XMLOutputFactoryImpl",
        )
        trySetSystemProperty(
            XMLEventFactory::class.qualifiedName!!,
            "com.sun.xml.internal.stream.events.XMLEventFactoryImpl",
        )
    }

    context(Settings)
    private fun RepositoryHandler.addDefaultAmperRepositoriesForDependencies() {
        mavenCentral()
        // For the Android plugin and dependencies
        google()
        // For other Gradle plugins
        gradlePluginPortal()
        // For dev versions of kotlin
        maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev")
        // For dev versions of compose plugin and dependencies
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        // For compose experimental builds
        maven("https://packages.jetbrains.team/maven/p/firework/dev")
        // Sonatype OSS Snapshot Repository
        maven("https://oss.sonatype.org/content/repositories/snapshots")

        // Apply repositories from project properties.
        projectProperties.dependencyResolutionManagement?.repositories?.let { repositories ->
            repositories.forEach { repository ->
                maven(repository)
            }
        }
    }
}
