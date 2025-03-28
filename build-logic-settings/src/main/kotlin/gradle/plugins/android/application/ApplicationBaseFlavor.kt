package gradle.plugins.android.application

import com.android.build.api.dsl.ApplicationBaseFlavor
import gradle.accessors.androidNamespace
import gradle.accessors.catalog.libs
import gradle.accessors.settings


import gradle.api.trySet
import gradle.plugins.android.flavor.BaseFlavor
import org.gradle.api.Project

/**
 * Shared properties between [ApplicationProductFlavor] and [ApplicationDefaultConfig]
 *
 * See [gradle.model.android.ProductFlavorDsl] and [gradle.model.android.DefaultConfigDsl] for more information.
 */
internal interface ApplicationBaseFlavor<T : ApplicationBaseFlavor> : BaseFlavor<T>, ApplicationVariantDimension<T> {

    /**
     * The application ID.
     *
     * See [Set the Application ID](https://developer.android.com/studio/build/application-id.html)
     */

    val applicationId: String?

    /**
     * Version code.
     *
     * See [Versioning Your Application](http://developer.android.com/tools/publishing/versioning.html)
     */

    val versionCode: Int?

    /**
     * Version name.
     *
     * See [Versioning Your Application](http://developer.android.com/tools/publishing/versioning.html)
     */
    val versionName: String?

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
    override fun applyTo(receiver: T) {
        super<BaseFlavor>.applyTo(receiver)
        super<ApplicationVariantDimension>.applyTo(receiver)

        receiver.applicationId = applicationId ?: project.androidNamespace
        receiver::versionCode trySet (versionCode
            ?: project.settings.libs.version("android.versionCode")?.toInt())
        receiver::versionName trySet (versionName ?: project.settings.libs.version("android.versionName"))
        receiver::targetSdk trySet (targetSdk ?: project.settings.libs.version("android.targetSdk")?.toInt())
        receiver::targetSdkPreview trySet targetSdkPreview
        receiver::maxSdk trySet maxSdk
    }
}
