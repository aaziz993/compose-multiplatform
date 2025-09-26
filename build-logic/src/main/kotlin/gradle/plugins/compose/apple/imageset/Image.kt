package gradle.plugins.compose.apple.imageset

import gradle.plugins.compose.apple.Asset
import kotlinx.serialization.Serializable

@Serializable
public data class Image(
    override val filename: String? = null,
    override val idiom: String,
    override val size: String,
    override val scale: String = "1x",
    val platform: String,
    val appearances: Set<Appearance> = setOf(Appearance()),
) : Asset
