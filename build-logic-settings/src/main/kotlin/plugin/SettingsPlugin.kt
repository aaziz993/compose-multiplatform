@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package plugin

import gradle.allLibs
import gradle.deepMerge
import gradle.isUrl
import gradle.projectProperties
import gradle.serialization.decodeFromAny
import gradle.serialization.encodeToAny
import gradle.trySetSystemProperty
import java.net.URI
import javax.xml.stream.XMLEventFactory
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLOutputFactory
import kotlinx.serialization.json.Json
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.file.Directory
import org.gradle.api.file.FileCollection
import org.gradle.api.initialization.Settings
import org.gradle.api.internal.artifacts.repositories.DefaultMavenArtifactRepository
import org.gradle.kotlin.dsl.maven
import org.jetbrains.amper.gradle.SLF4JProblemReporterContext
import org.tomlj.Toml
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.nodes.Tag
import org.yaml.snakeyaml.representer.Represent
import org.yaml.snakeyaml.representer.Representer
import plugin.model.dependency.asVersionCatalogUri
import plugin.project.ProjectPlugin
import plugin.project.gradle.develocity.DevelocityPlugin
import plugin.project.gradle.githooks.GitHooksPlugin
import plugin.project.gradle.toolchainmanagement.ToolchainManagementPlugin
import plugin.project.model.Properties

private const val PROJECT_PROPERTIES_FILE = "project.yaml"

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
                // at this point all projects have been created by settings.gradle.kts, but none were evaluated yet
                target.gradle.rootProject.allprojects {
                    configureProject()
                }
            }
        }
    }

    @Suppress("UnstableApiUsage")
    private fun Settings.setupProject() {
        projectProperties = layout.settingsDirectory.loadProperties()

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
                    addDependenciesRepositories()
                }

                dependencyResolutionManagement.versionCatalogs?.let { versionCatalogs ->
                    versionCatalogs {
                        val externalLibs = versionCatalogs.associate { (catalogName, dependency) ->
                            var notation = dependency.resolve()

                            create(catalogName) {
                                from(notation)
                            }


                            catalogName to if (notation is FileCollection) {
                                Toml.parse(notation.files.single().readText())
                            }
                            else {
                                notation as String

                                val cacheFile = layout.settingsDirectory.file("gradle/$catalogName.versions.toml").asFile

                                if (cacheFile.exists()) {
                                    return@associate catalogName to Toml.parse(cacheFile.readText())
                                }

                                if (!notation.isUrl) {
                                    notation = notation.asVersionCatalogUri()
                                }

                                repositories.map { (it as DefaultMavenArtifactRepository).url.toString().removeSuffix("/") }
                                    .firstNotNullOf { url ->
                                        try {
                                            Toml.parse(
                                                URI(
                                                    "$url/$notation",
                                                ).toURL().readText().also { catalogText ->
                                                    cacheFile.writeText(catalogText)
                                                },
                                            )
                                        }
                                        catch (_: Throwable) {
                                            null
                                        }
                                    }
                            }
                        }

                        val libsFile = layout.settingsDirectory.file("gradle/libs.versions.toml").asFile
                        val defaultLibs = if (!allLibs.containsKey("libs") && libsFile.exists())
                            mapOf("libs" to Toml.parse(libsFile.readText()))
                        else emptyMap()

                        allLibs = externalLibs + defaultLibs
                    }
                }
            }
        }

        // Apply plugins after version catalogs are loaded in settings phase.
        plugins.apply(DevelocityPlugin::class.java)
        plugins.apply(ToolchainManagementPlugin::class.java)
        plugins.apply(GitHooksPlugin::class.java)

        projectProperties.modules?.forEach(::include)
    }

    private fun Project.configureProject() {
        // Root projects properties already loaded in settings setup.
        projectProperties = layout.projectDirectory.loadProperties().apply {
            println("APPLY $PROJECT_PROPERTIES_FILE TO: $name")
            println(logYaml.dump(Json.Default.encodeToAny(this)))
        }

        plugins.apply(ProjectPlugin::class.java)

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
    private fun Directory.loadProperties(): Properties {
        val propertiesFile = file(PROJECT_PROPERTIES_FILE).asFile

        if (!propertiesFile.exists()) {
            return Properties()
        }

        val properties = yaml.load<MutableMap<String, *>>(propertiesFile.readText())

        val templatedProperties = (properties["templates"] as List<String>?)?.fold(emptyMap<String, Any?>()) { acc, template ->
            acc deepMerge yaml.load<MutableMap<String, *>>(file(template).asFile.readText())
        }.orEmpty() deepMerge properties

        return json.decodeFromAny<Properties>(templatedProperties)
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
    private fun RepositoryHandler.addDependenciesRepositories() {
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
