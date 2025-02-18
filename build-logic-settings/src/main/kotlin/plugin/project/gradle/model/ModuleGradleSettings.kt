package plugin.project.gradle.model

import kotlinx.serialization.Serializable
import plugin.project.gradle.apivalidation.model.ApiValidationSettings
import plugin.project.gradle.buildconfig.model.BuildConfigSettings
import plugin.project.gradle.doctor.model.DoctorSettings
import plugin.project.gradle.dokka.model.DokkaSettings
import plugin.project.gradle.kover.model.KoverSettings
import plugin.project.gradle.sonar.model.SonarSettings
import plugin.project.gradle.spotless.model.SpotlessSettings

@Serializable
internal data class ModuleGradleSettings(
    val doctor: DoctorSettings = DoctorSettings(),
    val buildConfig: BuildConfigSettings = BuildConfigSettings(),
    val spotless: SpotlessSettings = SpotlessSettings(),
    val kover: KoverSettings = KoverSettings(),
    val sonar: SonarSettings = SonarSettings(),
    val dokka: DokkaSettings = DokkaSettings(),
    val apiValidation: ApiValidationSettings = ApiValidationSettings(),
)
