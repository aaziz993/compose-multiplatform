package gradle.plugins.android.application.bundle

import com.android.build.api.dsl.BundleTexture
import gradle.reflect.trySet
import kotlinx.serialization.Serializable

/**
 * DSL object for configuring the App Bundle Texture options
 *
 * This is accessed via [Bundle.texture]
 */
@Serializable
internal data class BundleTexture(
    val enableSplit: Boolean? = null,
    /**
     * Specifies the default texture format to be used when it's not possible
     * to deliver a format targeted to the device. This is for example the case
     * for standalone APKs, generated for pre-Lollipop devices.
     *
     * If left empty or unspecified, the fallback folders (folders not containing
     * a #tcf suffix) will be delivered to pre-Lollipop devices.
     *
     * If the default format is set and not empty (for example, it's set to "etc2"),
     * and a pre-Lollipop device does not support this format, then the app will be
     * considered as not compatible and won't be installable.
     *
     * since 4.1.0
     */
    val defaultFormat: String? = null,
) {

    @Suppress("UnstableApiUsage")
    fun applyTo(receiver: BundleTexture) {
        receiver::enableSplit trySet enableSplit
        receiver::defaultFormat trySet defaultFormat
    }
}
