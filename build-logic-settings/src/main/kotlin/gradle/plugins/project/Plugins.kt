package gradle.plugins.project

import kotlinx.serialization.Serializable
import gradle.plugins.animalsniffer.model.AnimalSnifferSettings
import gradle.plugins.apivalidation.model.ApiValidationSettings
import gradle.plugins.buildconfig.model.BuildConfigSettings
import gradle.plugins.dependencycheck.model.DependencyCheckSettings
import gradle.plugins.develocity.model.DevelocitySettings
import gradle.plugins.doctor.model.DoctorSettings
import gradle.plugins.dokka.model.DokkaSettings
import gradle.plugins.githooks.model.GitHooksSettings
import gradle.plugins.karakum.model.KarakumSettings
import gradle.plugins.knit.model.KnitSettings
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
import gradle.plugins.kover.model.KoverSettings
import gradle.plugins.publish.model.PublishingSettings
import gradle.plugins.shadow.model.ShadowSettings
import gradle.plugins.signing.model.SigningSettings
import gradle.plugins.sonar.model.SonarSettings
import gradle.plugins.spotless.model.SpotlessSettings
import gradle.plugins.toolchainmanagement.model.ToolchainManagementSettings

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
