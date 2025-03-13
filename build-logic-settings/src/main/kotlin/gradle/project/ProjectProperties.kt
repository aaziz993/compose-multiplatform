package gradle.project

import gradle.api.publish.PublishingExtension
import gradle.buildcache.BuildCacheConfiguration
import gradle.collection.deepMerge
import gradle.collection.resolve
import gradle.initialization.ScriptHandler
import gradle.plugins.android.BaseExtension
import gradle.plugins.android.application.BaseAppModuleExtension
import gradle.plugins.android.library.LibraryExtension
import gradle.plugins.java.JavaPluginExtension
import gradle.plugins.java.application.JavaApplication
import gradle.plugins.kotlin.HasKotlinDependencies
import gradle.plugins.kotlin.KotlinSettings
import gradle.plugins.web.NodeJsEnvSpec
import gradle.plugins.web.js.karakum.KarakumSettings
import gradle.plugins.web.npm.NpmExtension
import gradle.plugins.web.yarn.YarnRootExtension
import gradle.serialization.decodeFromAny
import gradle.tasks.Task
import gradle.tasks.TaskTransformingSerializer
import java.util.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json
import org.gradle.api.file.Directory
import org.yaml.snakeyaml.Yaml
import plugins.apple.model.AppleSettings
import plugins.cmp.model.CMPSettings

internal const val PROJECT_PROPERTIES_FILE = "project.yaml"

@Serializable
internal data class ProjectProperties(
    val pluginManagement: PluginManagement? = null,
    val dependencyResolutionManagement: DependencyResolutionManagement? = null,
    val buildscript: ScriptHandler? = null,
    override val dependencies: List<@Serializable(with = DependencyTransformingSerializer::class) Dependency>? = null,
    val includes: List<String>? = null,
    val projects: List<ProjectDescriptor>? = null,
    val buildCache: BuildCacheConfiguration? = null,
    val gradleEnterpriseAccessKey: String? = null,
    val type: ProjectType = ProjectType.LIB,
    val layout: ProjectLayout = ProjectLayout.DEFAULT,
    val group: String? = null,
    val description: String? = null,
    val version: VersionSettings = VersionSettings(),
    val plugins: Plugins = Plugins(),
    val java: JavaPluginExtension? = null,
    val application: JavaApplication? = null,
    val kotlin: KotlinSettings = KotlinSettings(),
    @Transient
    var android: BaseExtension? = null,
    val apple: AppleSettings = AppleSettings(),
    val nodeJsEnv: NodeJsEnvSpec = NodeJsEnvSpec(),
    val yarn: YarnRootExtension = YarnRootExtension(),
    val npm: NpmExtension = NpmExtension(),
    val karakum: KarakumSettings = KarakumSettings(),
    val compose: CMPSettings = CMPSettings(),
    val tasks: List<@Serializable(with = TaskTransformingSerializer::class) Task>? = null,
    private val localPropertiesFile: String = "local.properties",
) : HasKotlinDependencies {

    @Transient
    val localProperties = Properties()

    companion object {

        val json = Json {
            ignoreUnknownKeys = true
        }

        val yaml = Yaml()

        @Suppress("UNCHECKED_CAST")
        fun Directory.load(): ProjectProperties {
            val propertiesFile = file(PROJECT_PROPERTIES_FILE).asFile

            return if (propertiesFile.exists()) {

                val properties = yaml.load<MutableMap<String, *>>(propertiesFile.readText())

                val templatedProperties = (properties["templates"] as List<String>?)?.fold(emptyMap<String, Any?>()) { acc, template ->
                    acc deepMerge yaml.load<MutableMap<String, *>>(file(template).asFile.readText())
                }.orEmpty() deepMerge properties

                json.decodeFromAny<ProjectProperties>(templatedProperties.resolve()).apply {
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
                    val file = file(localPropertiesFile).asFile
                    if (file.exists()) {
                        load(file.reader())
                    }
                }
            }
        }
    }
}
