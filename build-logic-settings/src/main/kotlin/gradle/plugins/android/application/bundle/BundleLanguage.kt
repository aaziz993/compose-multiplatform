package gradle.plugins.android.application.bundle

import com.android.build.api.dsl.BundleLanguage
import gradle.reflect.trySet
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

    fun applyTo(receiver: BundleLanguage) {
        receiver::enableSplit trySet enableSplit
    }
}
