package gradle.plugins.android.application

import com.android.build.api.dsl.ApplicationSingleVariant
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
) : SingleVariant<ApplicationSingleVariant> {

    override fun applyTo(recipient: ApplicationSingleVariant) {
        super.applyTo(recipient)

        publishApk?.takeIf { it }?.run { recipient.publishApk() }
    }
}
