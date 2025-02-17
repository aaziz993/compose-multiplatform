package plugin.project.gradle.develocity.model

import kotlinx.serialization.Serializable

@Serializable
internal data class Git(
    val repo: String,
    val skipTags: Boolean = false,
)
