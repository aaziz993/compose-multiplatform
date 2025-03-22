package gradle.plugins.kover.currentproject

import kotlinx.serialization.Serializable

@Serializable
internal data class CreateVariant(
    val variantName: String,
    val config: KoverVariantCreateConfig
)
