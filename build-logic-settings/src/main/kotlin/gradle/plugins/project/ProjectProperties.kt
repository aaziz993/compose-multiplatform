package gradle.plugins.project

import gradle.accessors.projectProperties
import gradle.accessors.settings
import gradle.api.initialization.DependencyResolutionManagement
import gradle.api.initialization.PluginManagement
import gradle.api.initialization.ProjectDescriptor
import gradle.api.initialization.ScriptHandler
import gradle.api.isCI
import gradle.api.publish.maven.MavenPomDeveloper
import gradle.api.publish.maven.MavenPomLicense
import gradle.api.publish.maven.MavenPomScm
import gradle.api.tasks.Task
import gradle.caching.BuildCacheConfiguration
import gradle.collection.SerializableOptionalAnyMap
import gradle.collection.deepMerge
import gradle.collection.resolve
import gradle.plugins.android.BaseExtension
import gradle.plugins.apple.model.AppleSettings
import gradle.plugins.compose.model.CMPSettings
import gradle.plugins.java.JavaPluginExtension
import gradle.plugins.java.application.JavaApplication
import gradle.plugins.kotlin.mpp.model.KotlinMultiplatformSettings
import gradle.plugins.kotlin.targets.web.node.NodeJsEnvSpec
import gradle.plugins.kotlin.targets.web.npm.NpmExtension
import gradle.plugins.kotlin.targets.web.yarn.YarnRootEnvSpec
import gradle.plugins.kotlin.targets.web.yarn.YarnRootExtension
import gradle.plugins.project.file.CodeOfConductFile
import gradle.plugins.project.file.ContributingFile
import gradle.plugins.project.file.LicenseFile
import gradle.plugins.project.file.LicenseHeaderFile
import gradle.plugins.project.file.ProjectFile
import gradle.serialization.decodeFromAny
import java.util.*
import kotlin.io.path.Path
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json
import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.initialization.Settings
import org.gradle.api.plugins.ExtraPropertiesExtension
import org.gradle.api.provider.ProviderFactory
import org.gradle.kotlin.dsl.extra
import org.yaml.snakeyaml.Yaml

internal const val PROJECT_PROPERTIES_FILE = "project.yaml"

@Serializable
internal data class ProjectProperties(
    val layout: ProjectLayout = ProjectLayout.Default,
    val group: String? = null,
    val description: String? = null,
    val version: VersionSettings = VersionSettings(),
    val year: String? = null,
    val developer: MavenPomDeveloper? = null,
    val license: MavenPomLicense? = null,
    val remote: MavenPomScm? = null,
    val licenseFile: LicenseFile? = null,
    val licenseHeaderFile: LicenseHeaderFile? = null,
    val codeOfConductFile: CodeOfConductFile? = null,
    val contributingFile: ContributingFile? = null,
    val buildscript: ScriptHandler? = null,
    val pluginManagement: PluginManagement? = null,
    val plugins: Plugins = Plugins(),
    val dependencyResolutionManagement: DependencyResolutionManagement? = null,
    val dependencies: Set<Dependency>? = null,
    val cacheRedirector: Boolean = true,
    val includes: Set<String>? = null,
    val includeFlat: Set<String>? = null,
    val projects: Set<ProjectDescriptor>? = null,
    val buildCache: BuildCacheConfiguration? = null,
    val java: JavaPluginExtension = JavaPluginExtension(),
    val application: JavaApplication? = null,
    val kotlin: KotlinMultiplatformSettings = KotlinMultiplatformSettings(),
    val android: BaseExtension? = null,
    val apple: AppleSettings = AppleSettings(),
    val nodeJsEnv: NodeJsEnvSpec = NodeJsEnvSpec(),
    val yarn: YarnRootExtension = YarnRootExtension(),
    val yarnRootEnv: YarnRootEnvSpec = YarnRootEnvSpec(),
    val npm: NpmExtension = NpmExtension(),
    val compose: CMPSettings = CMPSettings(),
    val tasks: LinkedHashSet<Task<out org.gradle.api.Task>>? = null,
    val projectFiles: Set<ProjectFile> = emptySet(),
    val ci: Set<CI> = emptySet(),
    val otherProperties: SerializableOptionalAnyMap,
) : Map<String, Any?> by otherProperties {

    val includesAsPaths: List<String>?
        get() = includes?.map { include -> include.replace(":", System.lineSeparator()) }

    @Transient
    val localProperties = Properties()

    companion object {

        val json = Json {
            ignoreUnknownKeys = true
        }

        val yaml = Yaml()

        context(Settings)
        @Suppress("UnstableApiUsage")
        fun load(): ProjectProperties {
            settings.extra.exportExtras()
            return load(
                settings.layout.settingsDirectory,
                settings.layout.settingsDirectory,
                settings.providers,
                settings.extra,
                settings.projectProperties.localProperties,
            )
        }

        context(Project)
        @Suppress("UnstableApiUsage")
        fun load(): ProjectProperties {
            project.extra.exportExtras()
            return load(
                project.layout.projectDirectory,
                project.settings.layout.settingsDirectory,
                project.providers,
                project.extra,
                project.projectProperties.localProperties,
            )
        }

        private fun ExtraPropertiesExtension.exportExtras() =
            properties.putAll(
                mapOf(
                    "isCI" to isCI,
                ),
            )

        @Suppress("UNCHECKED_CAST")
        private fun load(
            directory: Directory,
            settingsDirectory: Directory,
            providers: ProviderFactory,
            extra: ExtraPropertiesExtension,
            localProperties: Properties,
        ): ProjectProperties {
            val propertiesFile = directory.file(PROJECT_PROPERTIES_FILE).asFile

            return if (propertiesFile.exists()) {
                val properties = yaml.load<MutableMap<String, *>>(propertiesFile.readText())

                val templatesProperties = (properties["templates"] as List<String>?)?.loadTemplates(directory).orEmpty()

                (templatesProperties deepMerge properties).resolve(providers, extra, localProperties)
            }
            else {
                emptyMap()
            }.let { properties ->
                json.decodeFromAny<ProjectProperties>(properties).apply {
                    localProperties.apply {
                        val file = settingsDirectory.file("local.properties").asFile
                        if (file.exists()) {
                            load(file.reader())
                        }
                    }
                }
            }
        }

        /** Resolves deep templates.
         * Templates also can contain templates.
         */
        @Suppress("UNCHECKED_CAST")
        private fun List<String>.loadTemplates(directory: Directory): Map<String, Any?> =
            fold(emptyMap()) { acc, templatePath ->
                val template = yaml.load<MutableMap<String, *>>(directory.file(templatePath).asFile.readText())

                acc deepMerge (template["templates"] as List<String>?)?.map { subTemplatePath ->
                    Path(templatePath).resolve(subTemplatePath).normalize().toString()
                }?.loadTemplates(directory).orEmpty() deepMerge template
            }
    }
}
