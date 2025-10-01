package gradle.api.compose.apple.image

import kotlinx.serialization.Serializable

@Serializable
public data class Image(
    val filename: String? = null,
    val size: String? = null,
    val scale: String? = null,
    val appearances: Set<Appearance> = setOf(Appearance()),
)
