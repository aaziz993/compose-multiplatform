package gradle.model.android

import com.android.build.api.dsl.TestBaseFlavor
import com.android.build.api.dsl.VariantDimension
import gradle.trySet
import org.gradle.api.Project

/**
 * Shared properties between [TestProductFlavor] and [TestDefaultConfig]
 *
 * See [ProductFlavor] and [DefaultConfig] for more information.
 */
internal interface TestBaseFlavor :
    BaseFlavor,
    TestVariantDimension {

    /**
     * The target SDK version.
     * Setting this it will override previous calls of [targetSdk] and [targetSdkPreview] setters.
     * Only one of [targetSdk] and [targetSdkPreview] should be set.
     *
     * See [uses-sdk element documentation](http://developer.android.com/guide/topics/manifest/uses-sdk-element.html).
     */
    val targetSdk: Int?

    /**
     * The target SDK version.
     * Setting this it will override previous calls of [targetSdk] and [targetSdkPreview] setters.
     * Only one of [targetSdk] and [targetSdkPreview] should be set.
     *
     * See [uses-sdk element documentation](http://developer.android.com/guide/topics/manifest/uses-sdk-element.html).
     */
    val targetSdkPreview: String?

    /**
     * The maxSdkVersion, or null if not specified. This is only the value set on this produce
     * flavor.
     *
     * See [uses-sdk element documentation](http://developer.android.com/guide/topics/manifest/uses-sdk-element.html).
     */
    val maxSdk: Int?

    context(Project)
    override fun applyTo(dimension: VariantDimension) {
        super<BaseFlavor>.applyTo(dimension)
        super<TestVariantDimension>.applyTo(dimension)

        dimension as TestBaseFlavor

        dimension::targetSdk trySet targetSdk
        dimension::targetSdkPreview trySet targetSdkPreview
        dimension::maxSdk trySet maxSdk
    }
}
