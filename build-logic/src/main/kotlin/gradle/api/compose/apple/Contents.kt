package gradle.api.compose.apple

import gradle.api.compose.apple.image.Image
import kotlinx.serialization.Serializable

@Serializable
public data class Contents(
    val images: Set<Image>? = null,
    val assets: Set<Image>? = null,
    val layers: Set<Layer>? = null,
)
