package gradle.plugins.android.publish

import gradle.plugins.android.flavor.FlavorDimensionAndValues
import kotlinx.serialization.Serializable

@Serializable
internal data class MultipleVariantsSettings(
    override val allVariants: Boolean? = null,
    override val includeBuildTypeValues: Set<String>? = null,
    override val includeFlavorDimensionAndValues: List<FlavorDimensionAndValues>? = null,
    override val withSourcesJar: Boolean? = null,
    override val withJavadocJar: Boolean? = null,
    val componentName: String? = null,
) : MultipleVariants<com.android.build.api.dsl.MultipleVariants>
