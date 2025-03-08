package gradle.model.android

import org.gradle.api.Project

/** See [InternalCommonExtension] */
internal interface InternalTestedExtension<BuildFeaturesT : BuildFeatures,
    BuildTypeT : BuildType,
    DefaultConfigT : DefaultConfigDsl,
    ProductFlavorT : ProductFlavorDsl,
    AndroidResourcesT : AndroidResources,
    InstallationT : Installation>
    : TestedExtensionDsl,
    InternalCommonExtension<BuildFeaturesT, BuildTypeT, DefaultConfigT, ProductFlavorT, AndroidResourcesT, InstallationT> {

    context(Project)
    override fun applyTo() {
        super<TestedExtensionDsl>.applyTo()
        super<InternalCommonExtension>.applyTo()
    }
}
