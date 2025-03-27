package gradle.plugins.project

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
import gradle.api.tasks.TaskKeyTransformingSerializer
import gradle.caching.BuildCacheConfiguration
import gradle.collection.deepMerge
import gradle.collection.resolve
import gradle.plugins.android.BaseExtension
import gradle.plugins.apple.model.AppleSettings
import gradle.plugins.cmp.model.CMPSettings
import gradle.plugins.java.JavaPluginExtension
import gradle.plugins.java.application.JavaApplication
import gradle.plugins.kotlin.mpp.model.KotlinMultiplatformSettings
import gradle.plugins.project.file.CodeOfConductFile
import gradle.plugins.project.file.ContributingFile
import gradle.plugins.project.file.LicenseFile
import gradle.plugins.project.file.LicenseHeaderFile
import gradle.plugins.project.file.ProjectFile
import gradle.plugins.kotlin.targets.web.node.NodeJsEnvSpec
import gradle.plugins.kotlin.targets.web.npm.NpmExtension
import gradle.plugins.kotlin.targets.web.yarn.YarnRootEnvSpec
import gradle.plugins.kotlin.targets.web.yarn.YarnRootExtension
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
import org.gradle.internal.impldep.kotlinx.serialization.descriptors.elementNames
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
    val dependencies: Set<@Serializable(with = DependencyKeyTransformingSerializer::class) Dependency>? = null,
    val cacheRedirector: Boolean = true,
    val includes: Set<String>? = null,
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
    val tasks: LinkedHashSet<@Serializable(with = TaskKeyTransformingSerializer::class) Task<out org.gradle.api.Task>>? = null,
    private val localPropertiesFile: String = "local.properties",
    val projectFiles: List<ProjectFile> = emptyList(),
) {

    @Transient
    val localProperties = Properties()

    @Transient
    lateinit var otherProperties: Map<String, Any?>
        private set

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
                settings.providers,
                settings.extra,
                settings.projectProperties.localProperties,
            )
        }

        context(Project)
        fun load(): ProjectProperties {
            project.extra.exportExtras()
            return load(
                project.layout.projectDirectory,
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
            providers: ProviderFactory,
            extra: ExtraPropertiesExtension,
            localProperties: Properties,
        ): ProjectProperties {
            val propertiesFile = directory.file(PROJECT_PROPERTIES_FILE).asFile

            return if (propertiesFile.exists()) {
                val properties = yaml.load<MutableMap<String, *>>(propertiesFile.readText())

                ((properties["templates"] as List<String>?)?.fold(emptyMap<String, Any?>()) { acc, template ->
                    acc deepMerge yaml.load<MutableMap<String, *>>(directory.file(template).asFile.readText())
                }.orEmpty() deepMerge properties).resolve(providers, extra, localProperties)
            }
            else {
                emptyMap<String, Any?>()
            }.let { properties ->
                json.decodeFromAny<ProjectProperties>(properties).apply {

                    localProperties.apply {
                        val file = directory.file(localPropertiesFile).asFile
                        if (file.exists()) {
                            load(file.reader())
                        }
                    }

                    otherProperties = properties.filterKeys { key -> key !in ProjectProperties.serializer().descriptor.elementNames }
                }
            }
        }
    }
}
