package gradle.api.project

import gradle.api.BaseProperties
import gradle.api.BaseProperties.Companion.exportExtraProperties
import gradle.api.BaseProperties.Companion.loadLocalProperties
import gradle.api.BaseProperties.Companion.load
import gradle.api.Version
import gradle.api.artifacts.Dependency
import gradle.api.catalog.PluginNotationContentPolymorphicSerializer
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
import java.util.*
import klib.data.type.collection.DelegatedMapTransformingSerializer
import klib.data.type.collection.SerializableOptionalAnyMap
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra
import org.jetbrains.compose.internal.utils.localPropertiesFile
import org.jetbrains.kotlin.gradle.plugin.extraProperties

@KeepGeneratedSerializer
@Serializable(with = ProjectPropertiesTransformingSerializer::class)
internal data class ProjectProperties(
    override val delegate: SerializableOptionalAnyMap,
    override val buildscript: ScriptHandler? = null,
    override val plugins: Set<@Serializable(with = PluginNotationContentPolymorphicSerializer::class) Any>? = null,
    override val cacheRedirector: Boolean = true,
    val layout: ProjectLayout = ProjectLayout.Default,
    val year: String? = null,
    val group: String? = null,
    val description: String? = null,
    val version: Version = Version(),
    val dependencies: Set<Dependency>? = null,
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
    val karakum: KarakumExtension? = null,
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
) : BaseProperties {

    companion object {

        private const val PROJECT_PROPERTIES_FILE = "project.yaml"

        context(Project)
        fun load(): ProjectProperties {
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
            return load(
                PROJECT_PROPERTIES_FILE,
                project.layout.projectDirectory,
                project,
            )
        }
    }
}

private object ProjectPropertiesTransformingSerializer :
    DelegatedMapTransformingSerializer<ProjectProperties>(ProjectProperties.generatedSerializer())

internal var Project.localProperties: Properties
    get() = extraProperties[BaseProperties.LOCAL_PROPERTIES_EXT] as Properties
    private set(value) {
        extraProperties[BaseProperties.LOCAL_PROPERTIES_EXT] = value
    }

private const val PROJECT_PROPERTIES_EXT = "project.properties.ext"

internal var Project.projectProperties: ProjectProperties
    get() = extraProperties[PROJECT_PROPERTIES_EXT] as ProjectProperties
    private set(value) {
        extraProperties[PROJECT_PROPERTIES_EXT] = value
    }
