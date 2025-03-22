package gradle.project

import gradle.accessors.exportExtras
import gradle.accessors.projectProperties
import gradle.api.initialization.DependencyResolutionManagement
import gradle.api.initialization.PluginManagement
import gradle.api.initialization.ProjectDescriptor
import gradle.api.initialization.ScriptHandler
import gradle.api.isCI
import gradle.api.publish.maven.MavenPomDeveloper
import gradle.api.publish.maven.MavenPomLicense
import gradle.api.publish.maven.MavenPomScm
import gradle.api.tasks.Task
import gradle.api.tasks.TaskTransformingSerializer
import gradle.caching.BuildCacheConfiguration
import gradle.collection.deepMerge
import gradle.collection.resolve
import gradle.plugins.android.BaseExtension
import gradle.plugins.android.application.BaseAppModuleExtension
import gradle.plugins.android.library.LibraryExtension
import gradle.plugins.java.JavaPluginExtension
import gradle.plugins.java.application.JavaApplication
import gradle.plugins.kotlin.HasKotlinDependencies
import gradle.plugins.kotlin.KotlinSettings
import gradle.plugins.web.node.NodeJsEnvSpec
import gradle.plugins.web.npm.NpmExtension
import gradle.plugins.web.yarn.YarnRootEnvSpec
import gradle.plugins.web.yarn.YarnRootExtension
import gradle.project.file.CodeOfConductFile
import gradle.project.file.ContributingFile
import gradle.project.file.LicenseFile
import gradle.project.file.LicenseHeaderFile
import gradle.project.file.ProjectFile
import gradle.serialization.decodeFromAny
import java.util.*
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
import plugins.apple.model.AppleSettings
import plugins.cmp.model.CMPSettings

internal const val PROJECT_PROPERTIES_FILE = "project.yaml"

@Serializable
internal data class ProjectProperties(
    val type: ProjectType = ProjectType.LIB,
    val layout: ProjectLayout = ProjectLayout.DEFAULT,
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
    val dependencyResolutionManagement: DependencyResolutionManagement? = null,
    val cacheRedirector: Boolean = true,
    override val dependencies: List<@Serializable(with = DependencyTransformingSerializer::class) Dependency>? = null,
    val includes: Set<String>? = null,
    val projects: Set<ProjectDescriptor>? = null,
    val buildCache: BuildCacheConfiguration? = null,
    val plugins: Plugins = Plugins(),
    val java: JavaPluginExtension = JavaPluginExtension(),
    val application: JavaApplication? = null,
    val kotlin: KotlinSettings = KotlinSettings(),
    @Transient
    var android: BaseExtension? = null,
    val apple: AppleSettings = AppleSettings(),
    val nodeJsEnv: NodeJsEnvSpec = NodeJsEnvSpec(),
    val yarn: YarnRootExtension = YarnRootExtension(),
    val yarnRootEnv: YarnRootEnvSpec = YarnRootEnvSpec(),
    val npm: NpmExtension = NpmExtension(),
    val compose: CMPSettings = CMPSettings(),
    val tasks: List<@Serializable(with = TaskTransformingSerializer::class) Task>? = null,
    private val localPropertiesFile: String = "local.properties",
    val projectFiles: List<ProjectFile> = emptyList(),
) : HasKotlinDependencies {

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
            extra.exportExtras()
            return load(layout.settingsDirectory, providers, extra, projectProperties.localProperties)
        }

        context(Project)
        fun load(): ProjectProperties {
            extra.exportExtras()
            return load(layout.projectDirectory, providers, extra, projectProperties.localProperties)
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
            providers: ProviderFactory,
            extra: ExtraPropertiesExtension,
            localProperties: Properties,
        ): ProjectProperties {
            val propertiesFile = file(PROJECT_PROPERTIES_FILE).asFile

            return if (propertiesFile.exists()) {

                val properties = yaml.load<MutableMap<String, *>>(propertiesFile.readText())

                val templatedProperties = ((properties["templates"] as List<String>?)?.fold(emptyMap<String, Any?>()) { acc, template ->
                    acc deepMerge yaml.load<MutableMap<String, *>>(file(template).asFile.readText())
                }.orEmpty() deepMerge properties).resolve(providers, extra, localProperties)

                json.decodeFromAny<ProjectProperties>(templatedProperties).apply {
                    android = templatedProperties["android"]?.let { androidProperties ->
                        if (type == ProjectType.APP)
                            androidProperties.let { json.decodeFromAny<BaseAppModuleExtension>(it) }
                        else
                            androidProperties.let { json.decodeFromAny<LibraryExtension>(it) }
                    }
                }
            }
            else {
                ProjectProperties()
            }.apply {
                localProperties.apply {
                    val file = settingsDir.file(localPropertiesFile)
                    if (file.exists()) {
                        load(file.reader())
                    }
                }
            }
        }
    }
}
