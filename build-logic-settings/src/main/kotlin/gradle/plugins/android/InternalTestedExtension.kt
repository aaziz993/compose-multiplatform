package gradle.plugins.android

import com.android.build.api.dsl.AndroidResources
import com.android.build.api.dsl.BuildFeatures
import com.android.build.api.dsl.BuildType
import com.android.build.api.dsl.DefaultConfig
import com.android.build.api.dsl.Installation
import com.android.build.api.dsl.ProductFlavor
import org.gradle.api.Project

/** See [InternalCommonExtension] */
internal interface InternalTestedExtension<
    BuildFeaturesT : BuildFeatures,
    BuildTypeT : BuildType,
    DefaultConfigT : DefaultConfig,
    ProductFlavorT : ProductFlavor,
    AndroidResourcesT : AndroidResources,
    InstallationT : Installation
    >
    : TestedExtensionDsl,
    InternalCommonExtension<BuildFeaturesT, BuildTypeT, DefaultConfigT, ProductFlavorT, AndroidResourcesT, InstallationT> {

    context(Project)
    override fun applyTo() {
        super<TestedExtensionDsl>.applyTo()
        super<InternalCommonExtension>.applyTo()
    }
}
