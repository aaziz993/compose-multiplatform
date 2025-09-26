package gradle.plugins.compose.apple.imageset

import kotlinx.serialization.Serializable

@Serializable
public data class Appearance(
    val appearance: String = "luminosity",
    val value: String = ""
)
