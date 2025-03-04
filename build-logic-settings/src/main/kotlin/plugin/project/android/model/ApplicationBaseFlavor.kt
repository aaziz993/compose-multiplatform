package plugin.project.android.model

import com.android.build.api.dsl.ApplicationBaseFlavor
import com.android.build.api.dsl.VariantDimension
import gradle.trySet
import org.gradle.api.Project

/**
 * Shared properties between [ApplicationProductFlavor] and [ApplicationDefaultConfig]
 *
 * See [ProductFlavor] and [DefaultConfig] for more information.
 */
internal interface ApplicationBaseFlavor :
    BaseFlavor,
    ApplicationVariantDimension {

    /**
     * The application ID.
     *
     * See [Set the Application ID](https://developer.android.com/studio/build/application-id.html)
     */

    var applicationId: String?

    /**
     * Version code.
     *
     * See [Versioning Your Application](http://developer.android.com/tools/publishing/versioning.html)
     */

    var versionCode: Int?

    /**
     * Version name.
     *
     * See [Versioning Your Application](http://developer.android.com/tools/publishing/versioning.html)
     */
    var versionName: String?

    /**
     * The target SDK version.
     * Setting this it will override previous calls of [targetSdk] and [targetSdkPreview] setters.
     * Only one of [targetSdk] and [targetSdkPreview] should be set.
     *
     * See [uses-sdk element documentation](http://developer.android.com/guide/topics/manifest/uses-sdk-element.html).
     */

    var targetSdk: Int?

    /**
     * The target SDK version.
     * Setting this it will override previous calls of [targetSdk] and [targetSdkPreview] setters.
     * Only one of [targetSdk] and [targetSdkPreview] should be set.
     *
     * See [uses-sdk element documentation](http://developer.android.com/guide/topics/manifest/uses-sdk-element.html).
     */
    var targetSdkPreview: String?

    /**
     * The maxSdkVersion, or null if not specified. This is only the value set on this produce
     * flavor.
     *
     * See [uses-sdk element documentation](http://developer.android.com/guide/topics/manifest/uses-sdk-element.html).
     */

    var maxSdk: Int?

    context(Project)
    override fun applyTo(dimension: VariantDimension) {
        super<BaseFlavor>.applyTo(dimension)
        super<ApplicationVariantDimension>.applyTo(dimension)

        dimension as ApplicationBaseFlavor

        dimension::applicationId trySet applicationId
        dimension::versionCode trySet versionCode
        dimension::versionName trySet versionName
        dimension::targetSdk trySet targetSdk
        dimension::targetSdkPreview trySet targetSdkPreview
        dimension::maxSdk trySet maxSdk
    }
}
