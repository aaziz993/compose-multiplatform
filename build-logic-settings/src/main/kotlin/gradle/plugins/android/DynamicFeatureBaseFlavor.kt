package gradle.plugins.android

import com.android.build.api.dsl.DynamicFeatureBaseFlavor
import com.android.build.api.dsl.DynamicFeatureProductFlavor
import org.gradle.api.Project

/**
 * Shared properties between DSL objects [DynamicFeatureProductFlavor] and
 * [DynamicFeatureDefaultConfig]
 *
 * See [ProductFlavorDsl] and [DefaultConfigDsl] for more information.
 */
internal interface DynamicFeatureBaseFlavor<in T : DynamicFeatureBaseFlavor> :
    BaseFlavor<T>,
    DynamicFeatureVariantDimension<T> {

    context(Project)
    override fun applyTo(recipient: T) {
        super<BaseFlavor>.applyTo(recipient)
        super<DynamicFeatureVariantDimension>.applyTo(recipient)
    }
}
