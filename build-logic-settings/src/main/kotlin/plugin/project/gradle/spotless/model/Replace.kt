package plugin.project.gradle.spotless.model

import kotlinx.serialization.Serializable

@Serializable
internal data class Replace(
    val name: String,
    val original: String,
    val replacement: String
)
