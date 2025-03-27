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

    override fun applyTo(receiver: T) {
        allVariants?.takeIfTrue()?.act(receiver::allVariants)

        includeBuildTypeValues?.toTypedArray()?.let(receiver::includeBuildTypeValues)

        includeFlavorDimensionAndValues?.forEach { (dimension, values) ->
            receiver.includeFlavorDimensionAndValues(dimension, * values.toTypedArray())
        }
    }
}


