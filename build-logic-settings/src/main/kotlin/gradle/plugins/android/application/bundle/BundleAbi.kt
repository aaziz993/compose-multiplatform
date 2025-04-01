package gradle.plugins.android.application.bundle

import com.android.build.api.dsl.BundleAbi
import klib.data.type.reflection.trySet
import kotlinx.serialization.Serializable

/**
 * DSL object for configuring the App Bundle ABI options
 *
 * This is accessed via [Bundle.abi]
 */
@Serializable
internal data class BundleAbi(
    val enableSplit: Boolean? = null,
) {

    fun applyTo(receiver: BundleAbi) {
        receiver::enableSplit trySet enableSplit
    }
}
