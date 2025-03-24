package gradle.plugins.kover.currentproject

import kotlinx.serialization.Serializable

@Serializable
internal data class ProvidedVariant(
    val variantName: String,
    val config: KoverVariantConfigImpl
)
