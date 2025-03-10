package gradle.model.project

import gradle.model.android.application.ApplicationPublishing
import gradle.model.gradle.publish.PublishingExtension
import kotlinx.serialization.Serializable
import plugin.project.gradle.animalsniffer.model.AnimalSnifferSettings
import plugin.project.gradle.apivalidation.model.ApiValidationSettings
import plugin.project.gradle.buildconfig.model.BuildConfigSettings
import plugin.project.gradle.develocity.model.DevelocitySettings
import plugin.project.gradle.doctor.model.DoctorSettings
import plugin.project.gradle.dokka.model.DokkaSettings
import plugin.project.gradle.githooks.model.GitHooksSettings
import plugin.project.gradle.knit.model.KnitSettings
import plugin.project.gradle.kover.model.KoverSettings
import plugin.project.gradle.publish.model.PublishingSettings
import plugin.project.gradle.shadow.model.ShadowSettings
import plugin.project.gradle.sonar.model.SonarSettings
import plugin.project.gradle.spotless.model.SpotlessSettings
import plugin.project.gradle.toolchainmanagement.model.ToolchainManagementSettings
import plugin.project.kotlin.allopen.model.AllOpenSettings
import plugin.project.kotlin.apollo.model.ApolloSettings
import plugin.project.kotlin.atomicfu.model.AtomicFUSettings
import plugin.project.kotlin.ksp.model.KspSettings
import plugin.project.kotlin.ktorfit.model.KtorfitSettings
import plugin.project.kotlin.noarg.model.NoArgSettings
import plugin.project.kotlin.powerassert.model.PowerAssertSettings
import plugin.project.kotlin.room.model.RoomSettings
import plugin.project.kotlin.rpc.model.RpcSettings
import plugin.project.kotlin.serialization.model.SerializationSettings
import plugin.project.kotlin.sqldelight.model.SqlDelightSettings

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
