package plugin.project.gradle.model

import kotlinx.serialization.Serializable
import plugin.project.gradle.apivalidation.model.ApiValidationExtensionSettings
import plugin.project.gradle.doctor.model.DoctorExtensionSettings
import plugin.project.gradle.kover.model.KoverSettings
import plugin.project.gradle.spotless.model.SpotlessSettings

@Serializable
internal data class GradleSettings(
    val doctor: DoctorExtensionSettings = DoctorExtensionSettings(),
    val spotless: SpotlessSettings = SpotlessSettings(),
    val kover: KoverSettings = KoverSettings(),
    val apiValidation: ApiValidationExtensionSettings = ApiValidationExtensionSettings()
)
