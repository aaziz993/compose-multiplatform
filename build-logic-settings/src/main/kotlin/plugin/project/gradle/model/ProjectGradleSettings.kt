package plugin.project.gradle.model

import kotlinx.serialization.Serializable
import plugin.project.gradle.apivalidation.model.ApiValidationSettings
import plugin.project.gradle.buildconfig.model.BuildConfigSettings
import plugin.project.gradle.develocity.model.DevelocitySettings
import plugin.project.gradle.doctor.model.DoctorSettings
import plugin.project.gradle.dokka.model.DokkaModuleSettings
import plugin.project.gradle.kover.model.KoverSettings
import plugin.project.gradle.spotless.model.SpotlessSettings

@Serializable
internal data class ProjectGradleSettings(
    val develocity: DevelocitySettings = DevelocitySettings()
)
