package gradle.model.kotlin.kmp.jvm.android

import kotlinx.serialization.Serializable

@Serializable
internal data class MultipleVariantsSettings(
    override val allVariants: Boolean? = null,
    override val includeBuildTypeValues: List<String>? = null,
    override val includeFlavorDimensionAndValues: List<FlavorDimensionAndValues>? = null,
    override val withSourcesJar: Boolean? = null,
    override val withJavadocJar: Boolean? = null,
    val componentName: String? = null,
) : MultipleVariants
