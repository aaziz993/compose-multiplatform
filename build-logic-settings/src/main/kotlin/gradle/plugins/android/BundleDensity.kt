package gradle.plugins.android

import com.android.build.api.dsl.BundleDensity
import gradle.trySet
import kotlinx.serialization.Serializable

/**
 * DSL object for configuring the App Bundle Density options
 *
 * This is accessed via [Bundle.density]
 */
@Serializable
internal data class BundleDensity(
    val enableSplit: Boolean? = null,
) {

    fun applyTo(density: BundleDensity) {
        density::enableSplit trySet enableSplit
    }
}
