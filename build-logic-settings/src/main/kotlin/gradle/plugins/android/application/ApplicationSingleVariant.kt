package gradle.plugins.android.application

import com.android.build.api.dsl.ApplicationSingleVariant
import com.android.build.api.dsl.PublishingOptions
import gradle.plugins.android.SingleVariant
import kotlinx.serialization.Serializable

/**
 * Single variant publishing options for application projects.
 */
@Serializable
internal data class ApplicationSingleVariant(
    override val variantName: String,
    override val withSourcesJar: Boolean? = null,
    override val withJavadocJar: Boolean? = null,
    /**
     * Configure to publish this variant as APK artifact. Android Gradle Plugin would publish this
     * variant as AAB artifact if this function is not invoked.
     */
    val publishApk: Boolean? = null,
) : SingleVariant {

    override fun applyTo(recipient: PublishingOptions) {
        super.applyTo(options)

        options as ApplicationSingleVariant

        publishApk?.takeIf { it }?.run { options.publishApk() }
    }
}
