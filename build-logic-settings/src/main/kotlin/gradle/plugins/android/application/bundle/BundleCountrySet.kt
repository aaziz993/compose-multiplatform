package gradle.plugins.android.application.bundle

import com.android.build.api.dsl.BundleCountrySet
import gradle.api.trySet
import kotlinx.serialization.Serializable

/**
 * DSL object for configuring the App Bundle Country Set options
 *
 * This is accessed via [Bundle.countrySet]
 */
@Serializable
internal data class BundleCountrySet(
    val enableSplit: Boolean? = null,
    /**
     * Specifies the default country set value for the bundle. Used for filtering splits for
     * standalone, system and universal APKs.
     */
    val defaultSet: String? = null,
) {

    @Suppress("UnstableApiUsage")
    fun applyTo(recipient: BundleCountrySet) {
        recipient::enableSplit trySet enableSplit
        recipient::defaultSet trySet defaultSet
    }
}
