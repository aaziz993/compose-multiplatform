package gradle.plugins.android

import com.android.build.api.dsl.DynamicFeatureVariantDimension

/**
 * Shared properties between DSL objects that contribute to an dynamic feature variant.
 *
 * That is, [DynamicFeatureBuildType] and [DynamicFeatureProductFlavor] and
 * [DynamicFeatureDefaultConfig].
 */
internal interface DynamicFeatureVariantDimension<T : DynamicFeatureVariantDimension> : VariantDimension<T>
