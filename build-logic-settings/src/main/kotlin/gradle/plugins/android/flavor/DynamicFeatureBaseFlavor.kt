package gradle.plugins.android.flavor

import com.android.build.api.dsl.DynamicFeatureBaseFlavor
import com.android.build.api.dsl.DynamicFeatureProductFlavor
import gradle.plugins.android.flavor.DynamicFeatureVariantDimension
import org.gradle.api.Project

/**
 * Shared properties between DSL objects [DynamicFeatureProductFlavor] and
 * [gradle.plugins.android.defaultconfig.DynamicFeatureDefaultConfig]
 *
 * See [ProductFlavorDsl] and [gradle.plugins.android.defaultconfig.DefaultConfigDsl] for more information.
 */
internal interface DynamicFeatureBaseFlavor<T : DynamicFeatureBaseFlavor> :
    BaseFlavor<T>,
    DynamicFeatureVariantDimension<T> {

    context(Project)
    override fun applyTo(receiver: T) {
        super<BaseFlavor>.applyTo(receiver)
        super<DynamicFeatureVariantDimension>.applyTo(receiver)
    }
}
