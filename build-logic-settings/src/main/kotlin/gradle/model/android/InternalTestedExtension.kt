package gradle.model.android

import com.android.build.api.dsl.TestedExtension
import com.android.build.gradle.internal.dsl.InternalTestedExtension
import org.gradle.api.Project

/** See [InternalCommonExtension] */
internal interface InternalTestedExtension<BuildFeaturesT : BuildFeatures,
    BuildTypeT : BuildType,
    DefaultConfigT : DefaultConfigDsl,
    ProductFlavorT : ProductFlavor,
    AndroidResourcesT : AndroidResources,
    InstallationT : Installation>
    : TestedExtensionDsl,
    InternalCommonExtension<BuildFeaturesT, BuildTypeT, DefaultConfigT, ProductFlavorT, AndroidResourcesT, InstallationT> {

    context(Project)
    override fun applyTo(extension: TestedExtension) {
        super<TestedExtensionDsl>.applyTo(extension)

        extension as InternalTestedExtension<*, *, *, *, *, *>

        super<InternalCommonExtension>.applyTo(extension)
    }
}
