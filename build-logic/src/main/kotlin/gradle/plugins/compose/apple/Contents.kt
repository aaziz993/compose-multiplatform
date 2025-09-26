package gradle.plugins.compose.apple

import gradle.plugins.compose.apple.imageset.Image
import kotlinx.serialization.Serializable

@Serializable
public data class Contents(
    val images: Set<Image>? = null,
    val assets: Set<Image>? = null,
    val layers: Set<Layer>? = null,
)
