package gradle.api.project

import gradle.api.Properties
import gradle.api.Properties.Companion.exportExtraProperties
import gradle.api.Properties.Companion.load
import gradle.api.Properties.Companion.loadLocalProperties
import gradle.api.Properties.Companion.yaml
import gradle.api.Version
import gradle.api.ci.CI
import gradle.api.initialization.ScriptHandler
import gradle.api.publish.PublishingExtension
import gradle.api.tasks.Task
import gradle.plugins.android.BaseExtension
import gradle.plugins.animalsniffer.AnimalSnifferExtension
import gradle.plugins.apivalidation.ApiValidationExtension
import gradle.plugins.apple.AppleProjectExtension
import gradle.plugins.buildconfig.BuildConfigExtension
import gradle.plugins.compose.model.ComposeSettings
import gradle.plugins.dependencycheck.DependencyCheckExtension
import gradle.plugins.doctor.model.DoctorSettings
import gradle.plugins.dokka.model.DokkaSettings
import gradle.plugins.java.JavaPluginExtension
import gradle.plugins.java.application.JavaApplication
import gradle.plugins.karakum.KarakumExtension
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
import gradle.plugins.shadow.model.ShadowSettings
import gradle.plugins.signing.model.SigningSettings
import gradle.plugins.sonar.SonarExtension
import gradle.plugins.spotless.SpotlessExtension
import klib.data.type.serialization.json.encodeToAny
import klib.data.type.serialization.json.serializer.JsonOptionalAnySerializer
import klib.data.type.serialization.serializer.MapSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra
import org.jetbrains.compose.internal.utils.localPropertiesFile
import org.jetbrains.kotlin.gradle.plugin.extraProperties

@Serializable(with = ProjectPropertiesMapSerializer::class)
internal interface ProjectProperties : Properties {

    val layout: ProjectLayout

    val group: String?

    val description: String?

    val version: Version

    val dependencies: Set<Dependency>?

    val kotlin: KotlinMultiplatformSettings?

    val doctor: DoctorSettings?

    val dependencyCheck: DependencyCheckExtension?

    val buildconfig: BuildConfigExtension?

    val spotless: SpotlessExtension?

    val kover: KoverSettings?

    val sonar: SonarExtension?

    val dokka: DokkaSettings?

    val shadow: ShadowSettings?

    val apiValidation: ApiValidationExtension?

    val animalSniffer: AnimalSnifferExtension?

    val knit: KnitSettings?

    val ksp: KspExtension?

    val karakum: KarakumExtension?

    val allopen: AllOpenExtension?

    val noarg: NoArgExtension?

    val atomicfu: AtomicFuExtension?

    val serialization: SerializationSettings?

    val benchmark: BenchmarksExtension?

    val sqldelight: SqlDelightExtension?

    val room: RoomExtension?

    val rpc: RpcExtension?

    val ktorfit: KtorfitGradleConfiguration?

    val apollo: ApolloExtension?

    val powerAssert: PowerAssertGradleExtension?

    val java: JavaPluginExtension?

    val application: JavaApplication?

    val android: BaseExtension?

    val apple: AppleProjectExtension?

    val nodeJsEnv: NodeJsEnvSpec?

    val yarn: YarnRootExtension?

    val yarnRootEnv: YarnRootEnvSpec?

    val npm: NpmExtension?

    val compose: ComposeSettings?

    val publishing: PublishingExtension?

    val signing: SigningSettings?

    val cis: Set<CI>?

    val tasks: LinkedHashSet<Task<out org.gradle.api.Task>>?

    companion object {

        private const val PROJECT_PROPERTIES_FILE = "project.yaml"

        context(Project)
        fun load() {
            // Load local.properties.
            project.localProperties = loadLocalProperties(project.localPropertiesFile)
            // Export extras.
            // enable Default Kotlin Hierarchy.
            project.extraProperties["kotlin.mpp.applyDefaultHierarchyTemplate"] = "true"
            // ios Compose uses UiKit, so we need to explicitly enable it, since it is experimental.
            project.extraProperties["org.jetbrains.compose.experimental.uikit.enabled"] = "true"

            project.extraProperties["generateBuildableXcodeproj.skipKotlinFrameworkDependencies"] = "true"

            project.extra.exportExtraProperties()
            // Load project.yaml.
            project.projectProperties = load<ProjectProperties>(
                PROJECT_PROPERTIES_FILE,
                project.layout.projectDirectory,
                project,
            ).also { properties ->
                println("Load $PROJECT_PROPERTIES_FILE for project: $name")
                println(yaml.dump(Json.Default.encodeToAny(properties)))
            }
        }
    }
}

private object ProjectPropertiesMapSerializer :
    MapSerializer<Any?, ProjectProperties, ProjectPropertiesImpl>(
        ProjectPropertiesImpl.serializer(),
        JsonOptionalAnySerializer,
    )

@Serializable
private data class ProjectPropertiesImpl(
    override val buildscript: ScriptHandler? = null,
    override val plugins: Set<Any>? = null,
    override val cacheRedirector: Boolean = true,
    override val layout: ProjectLayout = ProjectLayout.Default,
    override val group: String? = null,
    override val description: String? = null,
    override val version: Version = Version(),
    override val dependencies: Set<Dependency>? = null,
    override val kotlin: KotlinMultiplatformSettings? = null,
    override val doctor: DoctorSettings? = null,
    override val dependencyCheck: DependencyCheckExtension? = null,
    override val buildconfig: BuildConfigExtension? = null,
    override val spotless: SpotlessExtension? = null,
    override val kover: KoverSettings? = null,
    override val sonar: SonarExtension? = null,
    override val dokka: DokkaSettings? = null,
    override val shadow: ShadowSettings? = null,
    override val apiValidation: ApiValidationExtension? = null,
    override val animalSniffer: AnimalSnifferExtension? = null,
    override val knit: KnitSettings? = null,
    override val ksp: KspExtension? = null,
    override val karakum: KarakumExtension? = null,
    override val allopen: AllOpenExtension? = null,
    override val noarg: NoArgExtension? = null,
    override val atomicfu: AtomicFuExtension? = null,
    override val serialization: SerializationSettings? = null,
    override val benchmark: BenchmarksExtension? = null,
    override val sqldelight: SqlDelightExtension? = null,
    override val room: RoomExtension? = null,
    override val rpc: RpcExtension? = null,
    override val ktorfit: KtorfitGradleConfiguration? = null,
    override val apollo: ApolloExtension? = null,
    override val powerAssert: PowerAssertGradleExtension? = null,
    override val java: JavaPluginExtension? = null,
    override val application: JavaApplication? = null,
    override val android: BaseExtension? = null,
    override val apple: AppleProjectExtension? = null,
    override val nodeJsEnv: NodeJsEnvSpec? = null,
    override val yarn: YarnRootExtension? = null,
    override val yarnRootEnv: YarnRootEnvSpec? = null,
    override val npm: NpmExtension? = null,
    override val compose: ComposeSettings? = null,
    override val publishing: PublishingExtension? = null,
    override val signing: SigningSettings? = null,
    override val cis: Set<CI>? = null,
    override val tasks: LinkedHashSet<Task<out org.gradle.api.Task>>? = null,
) : ProjectProperties, MutableMap<String, Any?> by mutableMapOf<String, Any?>()

internal var Project.localProperties: java.util.Properties
    get() = extraProperties[Properties.LOCAL_PROPERTIES_EXT] as java.util.Properties
    private set(value) {
        extraProperties[Properties.LOCAL_PROPERTIES_EXT] = value
    }

private const val PROJECT_PROPERTIES_EXT = "project.properties.ext"

internal var Project.projectProperties: ProjectProperties
    get() = extraProperties[PROJECT_PROPERTIES_EXT] as ProjectProperties
    private set(value) {
        extraProperties[PROJECT_PROPERTIES_EXT] = value
    }
