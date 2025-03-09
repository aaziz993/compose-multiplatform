package gradle.model.android

import com.android.build.api.dsl.BundleCodeTransparency
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
     * When the [SigningConfig] has all necessary values set, it will be used for signing
     * non-debuggable bundles using code transparency.
     *
     */
    val signing: SigningConfigDslImpl? = null,
) {

    context(Project)
    @Suppress("UnstableApiUsage")
    fun applyTo(transparency: BundleCodeTransparency) {
        signing?.applyTo(transparency.signing)
    }
}
