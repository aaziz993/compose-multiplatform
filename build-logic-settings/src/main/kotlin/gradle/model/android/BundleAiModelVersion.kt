package gradle.model.android

import com.android.build.api.dsl.BundleAiModelVersion
import gradle.trySet
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
    fun applyTo(version: BundleAiModelVersion) {
        version::enableSplit trySet enableSplit
        version::defaultVersion trySet defaultVersion
    }
}
