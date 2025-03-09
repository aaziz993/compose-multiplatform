package gradle.model.kotlin.kmp.jvm.android

/**
 * Internal extension of the DSL interface that overrides the properties to use the implementation
 * types, in order to enable the use of kotlin delegation from the original DSL classes
 * to the new implementations.
 */
internal interface InternalCommonExtension<
    BuildFeaturesT : BuildFeatures,
    BuildTypeT : BuildType,
    DefaultConfigT : DefaultConfigDsl,
    ProductFlavorT : ProductFlavorDsl,
    AndroidResourcesT : AndroidResources,
    InstallationT : Installation> :
    CommonExtension<
        BuildFeaturesT,
        BuildTypeT,
        DefaultConfigT,
        ProductFlavorT,
        AndroidResourcesT,
        InstallationT>
