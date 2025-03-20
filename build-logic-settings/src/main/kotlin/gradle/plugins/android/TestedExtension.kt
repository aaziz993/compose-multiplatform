package gradle.plugins.android

import com.android.build.api.dsl.BuildFeatures
import com.android.build.api.dsl.BuildType
import com.android.build.api.dsl.DefaultConfig
import com.android.build.api.dsl.ProductFlavor
import org.gradle.api.Project

/**
 * Provides test components that are common to [AppExtension], [gradle.model.android.library.LibraryExtension], and
 * [FeatureExtension].
 *
 * To learn more about testing Android projects, read
 * [Test your app](https://developer.android.com/studio/test/index.html)
 */
internal abstract class TestedExtension<
    BuildFeaturesT : BuildFeatures,
    BuildTypeT : BuildType,
    DefaultConfigT : DefaultConfig,
    ProductFlavorT : ProductFlavor,
    > : BaseExtension<
    BuildFeaturesT,
    BuildTypeT,
    DefaultConfigT,
    ProductFlavorT,
    >, TestedExtensionDsl {

    context(Project)
    override fun applyTo() {
        super<BaseExtension>.applyTo()
        super<TestedExtensionDsl>.applyTo()
    }
}
