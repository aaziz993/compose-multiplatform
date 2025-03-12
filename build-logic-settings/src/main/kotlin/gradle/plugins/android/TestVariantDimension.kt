package gradle.plugins.android

import com.android.build.api.dsl.TestVariantDimension
import gradle.trySet
import org.gradle.api.Project

/**
 * Shared properties between DSL objects that contribute to a separate-test-project variant.
 *
 * That is, [TestBuildType] and [TestProductFlavor] and [TestDefaultConfig].
 */
internal interface TestVariantDimension :
    VariantDimension {

    /**
     * Returns whether multi-dex is enabled.
     *
     * This can be null if the flag is not set, in which case the default value is used.
     */
    val multiDexEnabled: Boolean?

    /** The associated signing config or null if none are set on the variant dimension. */
    val signingConfig: ApkSigningConfig?

    context(Project)
    override fun applyTo(dimension: com.android.build.api.dsl.VariantDimension) {
        super.applyTo(dimension)

        dimension as TestVariantDimension

        dimension::multiDexEnabled trySet multiDexEnabled
        dimension::signingConfig trySet signingConfig?.toApkSigningConfig()
    }
}
