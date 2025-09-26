package gradle.plugins.compose.apple.image

import kotlinx.serialization.Serializable

@Serializable
public data class Appearance(
    val appearance: String = "luminosity",
    val value: String = ""
)
