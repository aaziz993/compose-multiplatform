package gradle.plugins.android

import com.android.build.api.dsl.DynamicFeatureProductFlavor
import com.android.build.api.dsl.VariantDimension
import org.gradle.api.Project

/**
 * Shared properties between DSL objects [DynamicFeatureProductFlavor] and
 * [DynamicFeatureDefaultConfig]
 *
 * See [ProductFlavorDsl] and [DefaultConfigDsl] for more information.
 */
internal interface DynamicFeatureBaseFlavor<in T : DynamicFeatureProductFlavor> :
    BaseFlavor<T>,
    DynamicFeatureVariantDimension<T> {

    context(Project)
    override fun applyTo(dimension: T) {
        super<BaseFlavor>.applyTo(dimension)
        super<DynamicFeatureVariantDimension>.applyTo(dimension)
    }
}
