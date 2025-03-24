package gradle.plugins.android.flavor

import com.android.build.api.dsl.TestBaseFlavor
import gradle.accessors.libs
import gradle.accessors.settings
import gradle.accessors.version
import gradle.accessors.versions
import gradle.api.trySet
import gradle.plugins.android.test.TestVariantDimension
import org.gradle.api.Project

/**
 * Shared properties between [gradle.plugins.android.test.TestProductFlavor] and [gradle.plugins.android.test.TestDefaultConfig]
 *
 * See [ProductFlavorDsl] and [gradle.plugins.android.defaultconfig.DefaultConfigDsl] for more information.
 */
internal interface TestBaseFlavor<T : TestBaseFlavor> : BaseFlavor<T>, TestVariantDimension<T> {

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

    context(project: Project)
    override fun applyTo(receiver: T) {
        super<BaseFlavor>.applyTo(receiver)
        super<TestVariantDimension>.applyTo(receiver)

        receiver::targetSdk trySet (targetSdk ?: project.settings.libs.versions.version("android.targetSdk")?.toInt())
        receiver::targetSdkPreview trySet targetSdkPreview
        receiver::maxSdk trySet maxSdk
    }
}
