package gradle.plugins.android

import com.android.build.api.dsl.BundleDeviceTier
import gradle.api.trySet
import kotlinx.serialization.Serializable

/**
 * DSL object for configuring the App Bundle Device Tier options
 *
 * This is accessed via [Bundle.deviceTier]
 */
@Serializable
internal data class BundleDeviceTier(
    val enableSplit: Boolean? = null,
    /**
     * Specifies the default device tier value for the bundle. Used for local-testing and generating
     * universal APKs.
     *
     */
    val defaultTier: String? = null,
) {

    @Suppress("UnstableApiUsage")
    fun applyTo(tier: BundleDeviceTier) {
        tier::enableSplit trySet enableSplit
        tier::defaultTier trySet defaultTier
    }
}
