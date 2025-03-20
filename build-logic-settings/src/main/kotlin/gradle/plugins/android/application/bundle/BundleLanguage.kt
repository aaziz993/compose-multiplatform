package gradle.plugins.android.application.bundle

import com.android.build.api.dsl.BundleLanguage
import gradle.api.trySet
import kotlinx.serialization.Serializable

/**
 * DSL object for configuring the App Bundle Language options
 *
 * This is accessed via [Bundle.language]
 */
@Serializable
internal data class BundleLanguage(
    val enableSplit: Boolean? = null,
) {

    fun applyTo(recipient: BundleLanguage) {
        recipient::enableSplit trySet enableSplit
    }
}
