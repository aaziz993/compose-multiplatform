package gradle.plugins.android

import com.android.build.api.dsl.TestBaseFlavor
import gradle.accessors.libs
import gradle.accessors.settings
import gradle.accessors.version
import gradle.accessors.versions
import gradle.api.trySet
import org.gradle.api.Project

/**
 * Shared properties between [TestProductFlavor] and [TestDefaultConfig]
 *
 * See [ProductFlavorDsl] and [DefaultConfigDsl] for more information.
 */
internal interface TestBaseFlavor<in T : TestBaseFlavor> : BaseFlavor<T>, TestVariantDimension<T> {

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
    override fun applyTo(recipient: T) {
        super<BaseFlavor>.applyTo(recipient)
        super<TestVariantDimension>.applyTo(recipient)

        recipient::targetSdk trySet (targetSdk ?: settings.libs.versions.version("android.targetSdk")?.toInt())
        recipient::targetSdkPreview trySet targetSdkPreview
        recipient::maxSdk trySet maxSdk
    }
}
