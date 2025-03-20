package gradle.plugins.android.flavor

import com.android.build.api.dsl.DynamicFeatureVariantDimension
import gradle.plugins.android.VariantDimension

/**
 * Shared properties between DSL objects that contribute to an dynamic feature variant.
 *
 * That is, [DynamicFeatureBuildType] and [DynamicFeatureProductFlavor] and
 * [gradle.plugins.android.defaultconfig.DynamicFeatureDefaultConfig].
 */
internal interface DynamicFeatureVariantDimension<T : DynamicFeatureVariantDimension> : VariantDimension<T>
