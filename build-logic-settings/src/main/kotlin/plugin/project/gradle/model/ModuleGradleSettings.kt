package plugin.project.gradle.model

import kotlinx.serialization.Serializable
import plugin.project.gradle.apivalidation.model.ApiValidationSettings
import plugin.project.gradle.buildconfig.model.BuildConfigSettings
import plugin.project.gradle.doctor.model.DoctorSettings
import plugin.project.gradle.dokka.model.DokkaModuleSettings
import plugin.project.gradle.kover.model.KoverSettings
import plugin.project.gradle.spotless.model.SpotlessSettings

@Serializable
internal data class ModuleGradleSettings(
    val doctor: DoctorSettings = DoctorSettings(),
    val spotless: SpotlessSettings = SpotlessSettings(),
    val kover: KoverSettings = KoverSettings(),
    val dokka: DokkaModuleSettings = DokkaModuleSettings(),
    val apiValidation: ApiValidationSettings = ApiValidationSettings(),
    val buildConfig: BuildConfigSettings = BuildConfigSettings()
)
