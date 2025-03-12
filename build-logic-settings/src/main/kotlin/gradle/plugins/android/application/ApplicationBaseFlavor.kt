package gradle.plugins.android.application

import com.android.build.api.dsl.ApplicationBaseFlavor
import com.android.build.api.dsl.VariantDimension
import gradle.accessors.androidNamespace
import gradle.accessors.libs
import gradle.accessors.settings
import gradle.accessors.version
import gradle.accessors.versions
import gradle.api.trySet
import gradle.plugins.android.BaseFlavor
import org.gradle.api.Project

/**
 * Shared properties between [ApplicationProductFlavor] and [ApplicationDefaultConfig]
 *
 * See [gradle.model.android.ProductFlavorDsl] and [gradle.model.android.DefaultConfigDsl] for more information.
 */
internal interface ApplicationBaseFlavor :
    BaseFlavor,
    ApplicationVariantDimension {

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
    override fun applyTo(dimension: VariantDimension) {
        super<BaseFlavor>.applyTo(dimension)
        super<ApplicationVariantDimension>.applyTo(dimension)

        dimension as ApplicationBaseFlavor

        dimension.applicationId = applicationId ?: androidNamespace
        dimension::versionCode trySet (versionCode ?: settings.libs.versions.version("android.versionCode")?.toInt())
        dimension::versionName trySet (versionName ?: settings.libs.versions.version("android.versionName"))
        dimension::targetSdk trySet (targetSdk ?: settings.libs.versions.version("android.targetSdk")?.toInt())
        dimension::targetSdkPreview trySet targetSdkPreview
        dimension::maxSdk trySet maxSdk
    }
}
