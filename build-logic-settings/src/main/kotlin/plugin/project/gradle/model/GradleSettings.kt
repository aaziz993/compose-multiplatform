package plugin.project.gradle.model

import kotlinx.serialization.Serializable
import plugin.project.gradle.apivalidation.model.ApiValidationSettings
import plugin.project.gradle.doctor.model.DoctorSettings
import plugin.project.gradle.spotless.model.SpotlessSettings

@Serializable
internal data class GradleSettings(
    val doctor: DoctorSettings = DoctorSettings(),
    val spotless: SpotlessSettings = SpotlessSettings(),
    val apiValidation: ApiValidationSettings = ApiValidationSettings()
)
