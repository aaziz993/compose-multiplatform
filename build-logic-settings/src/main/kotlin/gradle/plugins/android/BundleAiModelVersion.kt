package gradle.plugins.android

import com.android.build.api.dsl.BundleAiModelVersion
import gradle.api.trySet
import kotlinx.serialization.Serializable

/**
 * DSL object for configuring the App Bundle AI model version targeting
 *
 * This is accessed via [Bundle.aiModelVersion]
 */
@Serializable
internal data class BundleAiModelVersion(
    val enableSplit: Boolean? = null,
    /**
     * Specifies the default AI model version value for the bundle. Used for filtering splits for
     * standalone, system and universal APKs.
     */
    val defaultVersion: String? = null,
) {

    @Suppress("UnstableApiUsage")
    fun applyTo(recipient: BundleAiModelVersion) {
        recipient::enableSplit trySet enableSplit
        recipient::defaultVersion trySet defaultVersion
    }
}
