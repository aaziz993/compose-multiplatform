package gradle.plugins.compose.apple.imageset

import kotlinx.serialization.Serializable

@Serializable
public data class Image(
    val filename: String? = null,
    val size: String? = null,
    val scale: String = "1x",
    val appearances: Set<Appearance> = setOf(Appearance()),
)
