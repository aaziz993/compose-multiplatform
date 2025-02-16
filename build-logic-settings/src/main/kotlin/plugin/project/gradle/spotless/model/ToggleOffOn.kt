package plugin.project.gradle.spotless.model

import kotlinx.serialization.Serializable

@Serializable
internal data class ToggleOffOn(
    val off: String,
    val on: String
)
