package gradle.plugins.kover.currentproject

import kotlinx.serialization.Serializable

@Serializable
internal data class CopyVariant(
    val variantName: String,
    val originalVariantName: String
)
