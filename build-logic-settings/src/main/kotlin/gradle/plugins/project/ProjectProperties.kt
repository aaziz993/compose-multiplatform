package gradle.plugins.project

import gradle.accessors.localProperties
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
import java.io.File
import java.util.*
import kotlin.io.path.Path
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.descriptors.elementNames
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.json.jsonObject
import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.file.RegularFile
import org.gradle.api.initialization.Settings
import org.gradle.api.plugins.ExtraPropertiesExtension
import org.gradle.api.provider.ProviderFactory
import org.gradle.kotlin.dsl.extra
import org.jetbrains.compose.internal.utils.localPropertiesFile
import org.yaml.snakeyaml.Yaml

internal const val PROJECT_PROPERTIES_FILE = "project.yaml"

@KeepGeneratedSerializer
@Serializable(with = ProjectPropertiesTransformingSerializer::class)
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
    val delegate: SerializableOptionalAnyMap = emptyMap(),
) : Map<String, Any?> by delegate {

    val includesAsPaths by lazy {
        includes?.map { include -> include.replace(":", System.lineSeparator()) }
    }

    companion object {

        val json = Json {
            ignoreUnknownKeys = true
        }

        val yaml = Yaml()

        context(Settings)
        @Suppress("UnstableApiUsage")
        fun load(): ProjectProperties {
            // Load local.properties.
            localProperties = loadLocalProperties(
                layout.settingsDirectory
                    .file("local.properties").asFile,
            )
            // Export extras.
            settings.extra.exportExtras()
            // Load project.yaml.
            return load(
                settings.layout.settingsDirectory,
                settings.providers,
                settings.extra,
                settings.localProperties,
            )
        }

        context(Project)
        @Suppress("UnstableApiUsage")
        fun load(): ProjectProperties {
            // Load local.properties.
            localProperties = loadLocalProperties(
                project.localPropertiesFile,
            )
            // Export extras.
            project.extra.exportExtras()
            // Load project.yaml.
            return load(
                project.layout.projectDirectory,
                project.providers,
                project.extra,
                project.localProperties,
            )
        }

        private fun loadLocalProperties(file: File) =
            Properties().apply {
                file.takeIf(File::exists)
                    ?.reader()
                    ?.let(::load)
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

            if (propertiesFile.exists()) {
                val properties = yaml.load<MutableMap<String, *>>(propertiesFile.readText())

                val templatesProperties = (properties["templates"] as List<String>?)?.loadTemplates(directory).orEmpty()

                return json.decodeFromAny<ProjectProperties>((templatesProperties deepMerge properties).resolve(providers, extra, localProperties))
            }

            return ProjectProperties()
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

private object ProjectPropertiesTransformingSerializer
    : JsonTransformingSerializer<ProjectProperties>(ProjectProperties.generatedSerializer()) {

    override fun transformDeserialize(element: JsonElement): JsonElement = JsonObject(
        buildMap {
            putAll(element.jsonObject)

            val elementNames = ProjectProperties.generatedSerializer().descriptor.elementNames - "delegate"
            put("delegate", JsonObject(element.jsonObject.filterKeys { key -> key !in elementNames }))
        },
    )

    override fun transformSerialize(element: JsonElement): JsonElement = JsonObject(
        element.jsonObject.filterKeys { key -> key != "delegate" },
    )
}
