package gradle.plugins.android.publish

import com.android.build.api.dsl.MultipleVariants
import gradle.plugins.android.flavor.FlavorDimensionAndValues

/**
 * Multi variant publishing options.
 */
internal interface MultipleVariants<T : MultipleVariants> : PublishingOptions<T> {

    /**
     * Publish all the variants to the component.
     */
    val allVariants: Boolean?

    /**
     * Publish variants to the component based on the specified build types.
     */
    val includeBuildTypeValues: Set<String>?

    /**
     * Publish variants to the component based on the specified product flavor dimension and values.
     */
    val includeFlavorDimensionAndValues: List<FlavorDimensionAndValues>?

    override fun applyTo(recipient: T) {
        allVariants?.takeIf { it }?.run { recipient.allVariants() }

        includeBuildTypeValues?.toTypedArray()?.let(recipient::includeBuildTypeValues)

        includeFlavorDimensionAndValues?.forEach { (dimension, values) ->
            recipient.includeFlavorDimensionAndValues(dimension, * values.toTypedArray())
        }
    }
}


