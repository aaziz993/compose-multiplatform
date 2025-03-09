package gradle.model.kotlin.kmp.jvm.android

import com.android.build.api.dsl.BundleLanguage
import gradle.trySet
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

    fun applyTo(language: BundleLanguage) {
        language::enableSplit trySet enableSplit
    }
}
