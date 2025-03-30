package gradle.plugins.project

import gradle.accessors.localProperties
import gradle.api.ci.CI
import gradle.api.initialization.DependencyResolutionManagement
import gradle.api.initialization.PluginManagement
import gradle.api.initialization.ProjectDescriptor
import gradle.api.initialization.ScriptHandler
import gradle.api.publish.PublishingExtension
import gradle.api.publish.maven.MavenPomDeveloper
import gradle.api.publish.maven.MavenPomLicense
import gradle.api.publish.maven.MavenPomScm
import gradle.api.tasks.Task
import gradle.caching.BuildCacheConfiguration
import gradle.collection.SerializableOptionalAnyMap
import gradle.collection.deepMerge
import gradle.collection.resolve
import gradle.plugins.android.BaseExtension
import gradle.plugins.animalsniffer.AnimalSnifferExtension
import gradle.plugins.apivalidation.ApiValidationExtension
import gradle.plugins.apple.AppleProjectExtension
import gradle.plugins.buildconfig.BuildConfigExtension
import gradle.plugins.compose.model.ComposeSettings
import gradle.plugins.dependencycheck.DependencyCheckExtension
import gradle.plugins.develocity.model.DevelocitySettings
import gradle.plugins.doctor.model.DoctorSettings
import gradle.plugins.dokka.model.DokkaSettings
import gradle.plugins.githooks.GitHooksExtension
import gradle.plugins.githooks.model.GitHooksSettings
import gradle.plugins.java.JavaPluginExtension
import gradle.plugins.java.application.JavaApplication
import gradle.plugins.karakum.model.KarakumSettings
import gradle.plugins.knit.model.KnitSettings
import gradle.plugins.kotlin.allopen.AllOpenExtension
import gradle.plugins.kotlin.apollo.ApolloExtension
import gradle.plugins.kotlin.atomicfu.AtomicFuExtension
import gradle.plugins.kotlin.benchmark.BenchmarksExtension
import gradle.plugins.kotlin.ksp.KspExtension
import gradle.plugins.kotlin.ktorfit.KtorfitGradleConfiguration
import gradle.plugins.kotlin.mpp.model.KotlinMultiplatformSettings
import gradle.plugins.kotlin.noarg.NoArgExtension
import gradle.plugins.kotlin.powerassert.PowerAssertGradleExtension
import gradle.plugins.kotlin.room.RoomExtension
import gradle.plugins.kotlin.rpc.RpcExtension
import gradle.plugins.kotlin.serialization.model.SerializationSettings
import gradle.plugins.kotlin.sqldelight.SqlDelightExtension
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
import gradle.plugins.shadow.model.ShadowSettings
import gradle.plugins.signing.model.SigningSettings
import gradle.plugins.sonar.SonarExtension
import gradle.plugins.spotless.SpotlessExtension
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
import org.jetbrains.kotlin.gradle.plugin.extraProperties
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
    val projectFiles: Set<ProjectFile>? = null,
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
    val gitHooks: GitHooksExtension? = null,
    val kotlin: KotlinMultiplatformSettings? = null,
    val doctor: DoctorSettings? = null,
    val dependencyCheck: DependencyCheckExtension? = null,
    val buildconfig: BuildConfigExtension? = null,
    val spotless: SpotlessExtension? = null,
    val kover: KoverSettings? = null,
    val sonar: SonarExtension? = null,
    val dokka: DokkaSettings? = null,
    val shadow: ShadowSettings? = null,
    val apiValidation: ApiValidationExtension? = null,
    val animalSniffer: AnimalSnifferExtension? = null,
    val knit: KnitSettings? = null,
    val ksp: KspExtension? = null,
    val karakum: KarakumSettings? = null,
    val allopen: AllOpenExtension? = null,
    val noarg: NoArgExtension? = null,
    val atomicfu: AtomicFuExtension? = null,
    val serialization: SerializationSettings? = null,
    val benchmark: BenchmarksExtension? = null,
    val sqldelight: SqlDelightExtension? = null,
    val room: RoomExtension? = null,
    val rpc: RpcExtension? = null,
    val ktorfit: KtorfitGradleConfiguration? = null,
    val apollo: ApolloExtension? = null,
    val powerAssert: PowerAssertGradleExtension? = null,
    val java: JavaPluginExtension? = null,
    val application: JavaApplication? = null,
    val android: BaseExtension? = null,
    val apple: AppleProjectExtension? = null,
    val nodeJsEnv: NodeJsEnvSpec? = null,
    val yarn: YarnRootExtension? = null,
    val yarnRootEnv: YarnRootEnvSpec? = null,
    val npm: NpmExtension? = null,
    val compose: ComposeSettings? = null,
    val publishing: PublishingExtension? = null,
    val signing: SigningSettings? = null,
    val cis: Set<CI>? = null,
    val tasks: LinkedHashSet<Task<out org.gradle.api.Task>>? = null,
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
            // enable Default Kotlin Hierarchy.
            extraProperties["kotlin.mpp.applyDefaultHierarchyTemplate"] = "true"
            // ios Compose uses UiKit, so we need to explicitly enable it, since it is experimental.
            extraProperties["org.jetbrains.compose.experimental.uikit.enabled"] = "true"

            project.extraProperties["generateBuildableXcodeproj.skipKotlinFrameworkDependencies"] = "true"

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
