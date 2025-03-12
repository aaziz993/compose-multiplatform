package gradle.plugins.android

/**
 * Shared properties between DSL objects that contribute to an dynamic feature variant.
 *
 * That is, [DynamicFeatureBuildType] and [DynamicFeatureProductFlavor] and
 * [DynamicFeatureDefaultConfig].
 */
internal interface DynamicFeatureVariantDimension : VariantDimension
