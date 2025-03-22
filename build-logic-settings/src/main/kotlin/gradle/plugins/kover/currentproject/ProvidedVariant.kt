package gradle.plugins.kover.currentproject

import kotlinx.kover.gradle.plugin.dsl.KoverVariantCreateConfig
import kotlinx.serialization.Serializable
import org.gradle.api.Action

@Serializable
internal data class ProvidedVariant(
    val variantName: String,
    val config: KoverVariantConfigImpl
)
