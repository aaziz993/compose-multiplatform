package gradle.plugins.project

import gradle.accessors.localProperties
import gradle.api.ci.CI
import gradle.api.initialization.DependencyResolutionManagement
import gradle.api.initialization.PluginManagement
import gradle.api.initialization.ProjectDescriptor
import gradle.api.initialization.ScriptHandler
import gradle.api.publish.maven.MavenPomDeveloper
import gradle.api.publish.maven.MavenPomLicense
import gradle.api.publish.maven.MavenPomScm
import gradle.api.tasks.Task
import gradle.caching.BuildCacheConfiguration
import gradle.collection.SerializableOptionalAnyMap
import gradle.collection.deepMerge
import gradle.collection.resolve
import gradle.plugins.android.BaseExtension
import gradle.plugins.animalsniffer.model.AnimalSnifferSettings
import gradle.plugins.apivalidation.model.ApiValidationSettings
import gradle.plugins.apple.model.AppleSettings
import gradle.plugins.buildconfig.model.BuildConfigSettings
import gradle.plugins.compose.model.CMPSettings
import gradle.plugins.dependencycheck.model.DependencyCheckSettings
import gradle.plugins.develocity.model.DevelocitySettings
import gradle.plugins.doctor.model.DoctorSettings
import gradle.plugins.dokka.model.DokkaSettings
import gradle.plugins.githooks.model.GitHooksSettings
import gradle.plugins.java.JavaPluginExtension
import gradle.plugins.java.application.JavaApplication
import gradle.plugins.karakum.model.KarakumSettings
import gradle.plugins.knit.model.KnitSettings
import gradle.plugins.kotlin.allopen.model.AllOpenSettings
import gradle.plugins.kotlin.apollo.model.ApolloSettings
import gradle.plugins.kotlin.atomicfu.model.AtomicFUSettings
import gradle.plugins.kotlin.benchmark.model.BenchmarkSettings
import gradle.plugins.kotlin.ksp.model.KspSettings
import gradle.plugins.kotlin.ktorfit.model.KtorfitSettings
import gradle.plugins.kotlin.mpp.model.KotlinMultiplatformSettings
import gradle.plugins.kotlin.noarg.model.NoArgSettings
import gradle.plugins.kotlin.powerassert.model.PowerAssertSettings
import gradle.plugins.kotlin.room.model.RoomSettings
import gradle.plugins.kotlin.rpc.model.RpcSettings
import gradle.plugins.kotlin.serialization.model.SerializationSettings
import gradle.plugins.kotlin.sqldelight.model.SqlDelightSettings
import gradle.plugins.kotlin.targets.web.node.NodeJsEnvSpec
import gradle.plugins.kotlin.targets.web.npm.NpmExtension
import gradle.plugins.kotlin.targets.web.yarn.YarnRootEnvSpec
import gradle.plugins.kotlin.targets.web.yarn.YarnRootExtension
import gradle.plugins.kover.model.KoverSettings
import gradle.plugins.project.file.CodeOfConductFile
import gradle.plugins.project.file.ContributingFile
import gradle.plugins.project.file.LicenseFile
import gradle.plugins.project.file.LicenseHeaderFile
import gradle.plugins.project.file.ProjectFile
import gradle.plugins.publish.model.PublishingSettings
import gradle.plugins.shadow.model.ShadowSettings
import gradle.plugins.signing.model.SigningSettings
import gradle.plugins.sonar.model.SonarSettings
import gradle.plugins.spotless.model.SpotlessSettings
import gradle.plugins.toolchainmanagement.model.ToolchainManagementSettings
import gradle.serialization.decodeFromAny
import java.io.File
import java.util.*
import kotlin.io.path.Path
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.elementNames
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.json.jsonObject
import org.gradle.api.Project
import org.gradle.api.file.Directory
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
    val year: String? = null,
    val group: String? = null,
    val description: String? = null,
    val version: VersionSettings = VersionSettings(),
    val remote: MavenPomScm? = null,
    val developer: MavenPomDeveloper? = null,
    val license: MavenPomLicense? = null,
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
    val develocity: DevelocitySettings? = null,
    val toolchainManagement: ToolchainManagementSettings? = null,
    val gitHooks: GitHooksSettings? = null,
    val doctor: DoctorSettings? = null,
    val dependencyCheck: DependencyCheckSettings? = null,
    val buildConfig: BuildConfigSettings? = null,
    val spotless: SpotlessSettings? = null,
    val kover: KoverSettings? = null,
    val sonar: SonarSettings? = null,
    val dokka: DokkaSettings? = null,
    val shadow: ShadowSettings? = null,
    val apiValidation: ApiValidationSettings? = null,
    val animalSniffer: AnimalSnifferSettings? = null,
    val knit: KnitSettings? = null,
    val publishing: PublishingSettings? = null,
    val signing: SigningSettings? = null,
    val ksp: KspSettings? = null,
    val karakum: KarakumSettings? = null,
    val allOpen: AllOpenSettings? = null,
    val noArg: NoArgSettings? = null,
    val atomicFU: AtomicFUSettings? = null,
    val serialization: SerializationSettings? = null,
    val benchmark: BenchmarkSettings? = null,
    val ktorfit: KtorfitSettings? = null,
    val apollo: ApolloSettings? = null,
    val rpc: RpcSettings? = null,
    val sqldelight: SqlDelightSettings? = null,
    val room: RoomSettings? = null,
    val powerAssert: PowerAssertSettings? = null,
    val java: JavaPluginExtension? = null,
    val application: JavaApplication? = null,
    val kotlin: KotlinMultiplatformSettings? = null,
    val android: BaseExtension? = null,
    val apple: AppleSettings? = null,
    val nodeJsEnv: NodeJsEnvSpec? = null,
    val yarn: YarnRootExtension? = null,
    val yarnRootEnv: YarnRootEnvSpec? = null,
    val npm: NpmExtension? = null,
    val compose: CMPSettings? = null,
    val tasks: LinkedHashSet<Task<out org.gradle.api.Task>>? = null,
    val projectFiles: Set<ProjectFile>? = null,
    val cis: Set<CI>? = null,
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
                    "ci" to buildMap {
                        put("present", CI.present)
                        CI.name?.let { name -> put("name", name) }
                    },
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
