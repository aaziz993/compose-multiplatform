package gradle.plugins.android.application.bundle

import com.android.build.api.dsl.BundleDeviceTier
import klib.data.type.reflection.trySet
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
    fun applyTo(receiver: BundleDeviceTier) {
        receiver::enableSplit trySet enableSplit
        receiver::defaultTier trySet defaultTier
    }
}
