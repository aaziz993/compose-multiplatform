package gradle.plugins.android.application.bundle

import com.android.build.api.dsl.BundleCountrySet
import gradle.reflect.trySet
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
    fun applyTo(receiver: BundleCountrySet) {
        receiver::enableSplit trySet enableSplit
        receiver::defaultSet trySet defaultSet
    }
}
