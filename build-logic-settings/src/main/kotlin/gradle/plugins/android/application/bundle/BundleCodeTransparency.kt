package gradle.plugins.android.application.bundle

import com.android.build.api.dsl.BundleCodeTransparency
import gradle.plugins.android.signing.SigningConfigDslImpl
import kotlinx.serialization.Serializable
import org.gradle.api.Project

/**
 * DSL object for configuring the App Bundle Code Transparency options
 *
 * This is accessed via [Bundle.codeTransparency]
 */
@Serializable
internal data class BundleCodeTransparency(

    /**
     * Specifies the signing configuration for the code transparency feature of `bundletool`.
     *
     * When the [gradle.plugins.android.signing.SigningConfig] has all necessary values set, it will be used for signing
     * non-debuggable bundles using code transparency.
     *
     */
    val signing: SigningConfigDslImpl? = null,
) {

    context(Project)
    @Suppress("UnstableApiUsage")
    fun applyTo(receiver: BundleCodeTransparency) {
        signing?.applyTo(receiver.signing)
    }
}
