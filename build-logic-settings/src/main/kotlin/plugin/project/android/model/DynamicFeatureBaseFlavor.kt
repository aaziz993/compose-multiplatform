package plugin.project.android.model

import com.android.build.api.dsl.VariantDimension
import org.gradle.api.Project

/**
 * Shared properties between DSL objects [DynamicFeatureProductFlavor] and
 * [DynamicFeatureDefaultConfig]
 *
 * See [ProductFlavor] and [DefaultConfig] for more information.
 */
internal interface DynamicFeatureBaseFlavor :
    BaseFlavor,
    DynamicFeatureVariantDimension {

    context(Project)
    override fun applyTo(dimension: VariantDimension) {
        super<BaseFlavor>.applyTo(dimension)
        super<DynamicFeatureVariantDimension>.applyTo(dimension)
    }
}
