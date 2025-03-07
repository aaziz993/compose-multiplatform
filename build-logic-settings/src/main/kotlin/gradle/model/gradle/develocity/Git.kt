package gradle.model.gradle.develocity

import kotlinx.serialization.Serializable

@Serializable
internal data class Git(
    val repo: String,
    val skipTags: Boolean = false,
)
