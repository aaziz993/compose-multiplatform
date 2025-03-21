package gradle.plugins.develocity.buildscan

import kotlinx.serialization.Serializable

@Serializable
internal data class Link(
    val name: String,
    val url: String,
)
