package gradle.project

import kotlinx.serialization.Serializable
import plugins.animalsniffer.model.AnimalSnifferSettings
import plugins.apivalidation.model.ApiValidationSettings
import plugins.buildconfig.model.BuildConfigSettings
import plugins.dependencycheck.model.DependencyCheckSettings
import plugins.develocity.model.DevelocitySettings
import plugins.doctor.model.DoctorSettings
import plugins.dokka.model.DokkaSettings
import plugins.githooks.model.GitHooksSettings
import plugins.karakum.model.KarakumSettings
import plugins.knit.model.KnitSettings
import plugins.kotlin.allopen.model.AllOpenSettings
import plugins.kotlin.apollo.model.ApolloSettings
import plugins.kotlin.atomicfu.model.AtomicFUSettings
import plugins.kotlin.benchmark.model.BenchmarkSettings
import plugins.kotlin.ksp.model.KspSettings
import plugins.kotlin.ktorfit.model.KtorfitSettings
import plugins.kotlin.noarg.model.NoArgSettings
import plugins.kotlin.powerassert.model.PowerAssertSettings
import plugins.kotlin.room.model.RoomSettings
import plugins.kotlin.rpc.model.RpcSettings
import plugins.kotlin.serialization.model.SerializationSettings
import plugins.kotlin.sqldelight.model.SqlDelightSettings
import plugins.kover.model.KoverSettings
import plugins.publish.model.PublishingSettings
import plugins.shadow.model.ShadowSettings
import plugins.signing.model.SigningSettings
import plugins.sonar.model.SonarSettings
import plugins.spotless.model.SpotlessSettings
import plugins.toolchainmanagement.model.ToolchainManagementSettings

@Serializable
internal data class Plugins(
    val develocity: DevelocitySettings = DevelocitySettings(),
    val toolchainManagement: ToolchainManagementSettings = ToolchainManagementSettings(),
    val gitHooks: GitHooksSettings = GitHooksSettings(),
    val doctor: DoctorSettings = DoctorSettings(),
    val dependencyCheck: DependencyCheckSettings = DependencyCheckSettings(),
    val buildConfig: BuildConfigSettings = BuildConfigSettings(),
    val spotless: SpotlessSettings = SpotlessSettings(),
    val kover: KoverSettings = KoverSettings(),
    val sonar: SonarSettings = SonarSettings(),
    val dokka: DokkaSettings = DokkaSettings(),
    val shadow: ShadowSettings = ShadowSettings(),
    val apiValidation: ApiValidationSettings = ApiValidationSettings(),
    val animalSniffer: AnimalSnifferSettings = AnimalSnifferSettings(),
    val knit: KnitSettings = KnitSettings(),
    val publishing: PublishingSettings = PublishingSettings(),
    val signing: SigningSettings = SigningSettings(),
    val ksp: KspSettings = KspSettings(),
    val karakum: KarakumSettings = KarakumSettings(),
    val allOpen: AllOpenSettings = AllOpenSettings(),
    val noArg: NoArgSettings = NoArgSettings(),
    val atomicFU: AtomicFUSettings = AtomicFUSettings(),
    val serialization: SerializationSettings = SerializationSettings(),
    val benchmark: BenchmarkSettings = BenchmarkSettings(),
    val ktorfit: KtorfitSettings = KtorfitSettings(),
    val apollo: ApolloSettings = ApolloSettings(),
    val rpc: RpcSettings = RpcSettings(),
    val sqldelight: SqlDelightSettings = SqlDelightSettings(),
    val room: RoomSettings = RoomSettings(),
    val powerAssert: PowerAssertSettings = PowerAssertSettings(),
)
