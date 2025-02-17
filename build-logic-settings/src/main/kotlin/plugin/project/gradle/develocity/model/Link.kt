package plugin.project.gradle.develocity.model

import kotlinx.serialization.Serializable

@Serializable
internal data class Link(
    val name:String,
    val url:String,
)
