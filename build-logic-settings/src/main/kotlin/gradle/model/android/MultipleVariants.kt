package gradle.model.android

import com.android.build.api.dsl.MultipleVariants

/**
 * Multi variant publishing options.
 */
internal interface MultipleVariants : PublishingOptions {

    /**
     * Publish all the variants to the component.
     */
    val allVariants: Boolean?

    /**
     * Publish variants to the component based on the specified build types.
     */
    val includeBuildTypeValues: List<String>?

    /**
     * Publish variants to the component based on the specified product flavor dimension and values.
     */
    val includeFlavorDimensionAndValues: List<FlavorDimensionAndValues>?

    fun applyTo(variants: MultipleVariants) {
        allVariants?.takeIf { it }?.run { variants.allVariants() }

        includeBuildTypeValues?.let { includeBuildTypeValues ->
            variants.includeBuildTypeValues(*includeBuildTypeValues.toTypedArray())
        }

        includeFlavorDimensionAndValues?.forEach { (dimension, values) ->
            variants.includeFlavorDimensionAndValues(dimension, * values.toTypedArray())
        }
    }
}


