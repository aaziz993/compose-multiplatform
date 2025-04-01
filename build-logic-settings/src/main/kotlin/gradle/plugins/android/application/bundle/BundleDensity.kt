package gradle.plugins.android.application.bundle

import com.android.build.api.dsl.BundleDensity
import klib.data.type.reflection.trySet
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

    fun applyTo(receiver: BundleDensity) {
        receiver::enableSplit trySet enableSplit
    }
}
