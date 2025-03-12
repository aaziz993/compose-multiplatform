package gradle.plugins.spotless

import kotlinx.serialization.Serializable

@Serializable
internal data class Replace(
    val name: String,
    val original: String,
    val replacement: String
)
