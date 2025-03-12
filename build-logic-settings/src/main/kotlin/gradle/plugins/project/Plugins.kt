package gradle.plugins.project

import gradle.plugins.android.application.ApplicationPublishing
import gradle.plugins.publish.PublishingExtension
import kotlinx.serialization.Serializable
import plugins.gradle.animalsniffer.model.AnimalSnifferSettings
import plugins.gradle.apivalidation.model.ApiValidationSettings
import plugins.gradle.buildconfig.model.BuildConfigSettings
import plugins.gradle.develocity.model.DevelocitySettings
import plugins.gradle.doctor.model.DoctorSettings
import plugins.gradle.dokka.model.DokkaSettings
import plugins.gradle.githooks.model.GitHooksSettings
import plugins.gradle.knit.model.KnitSettings
import plugins.gradle.kover.model.KoverSettings
import plugins.gradle.publish.model.PublishingSettings
import plugins.gradle.shadow.model.ShadowSettings
import plugins.gradle.sonar.model.SonarSettings
import plugins.gradle.spotless.model.SpotlessSettings
import plugins.gradle.toolchainmanagement.model.ToolchainManagementSettings
import plugins.kotlin.allopen.model.AllOpenSettings
import plugins.kotlin.apollo.model.ApolloSettings
import plugins.kotlin.atomicfu.model.AtomicFUSettings
import plugins.kotlin.ksp.model.KspSettings
import plugins.kotlin.ktorfit.model.KtorfitSettings
import plugins.kotlin.noarg.model.NoArgSettings
import plugins.kotlin.powerassert.model.PowerAssertSettings
import plugins.kotlin.room.model.RoomSettings
import plugins.kotlin.rpc.model.RpcSettings
import plugins.kotlin.serialization.model.SerializationSettings
import plugins.kotlin.sqldelight.model.SqlDelightSettings

@Serializable
internal data class Plugins(
    val develocity: DevelocitySettings = DevelocitySettings(),
    val toolchainManagement: ToolchainManagementSettings = ToolchainManagementSettings(),
    val gitHooks: GitHooksSettings = GitHooksSettings(),
    val doctor: DoctorSettings = DoctorSettings(),
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
    val ksp: KspSettings = KspSettings(),
    val allOpen: AllOpenSettings = AllOpenSettings(),
    val noArg: NoArgSettings = NoArgSettings(),
    val atomicFU: AtomicFUSettings = AtomicFUSettings(),
    val serialization: SerializationSettings = SerializationSettings(),
    val ktorfit: KtorfitSettings = KtorfitSettings(),
    val apollo: ApolloSettings = ApolloSettings(),
    val rpc: RpcSettings = RpcSettings(),
    val sqldelight: SqlDelightSettings = SqlDelightSettings(),
    val room: RoomSettings = RoomSettings(),
    val powerAssert: PowerAssertSettings = PowerAssertSettings(),
)
