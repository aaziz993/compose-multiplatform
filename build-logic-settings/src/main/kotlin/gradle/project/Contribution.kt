package gradle.project

import kotlinx.serialization.Serializable

@Serializable
internal data class Contribution(
    val name: String? = null,
    val url: String? = null,
    val distribution: String? = null,
    val comments: String? = null,
)
