package gradle.model.android

import com.android.build.api.dsl.BundleAbi
import gradle.trySet
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

    fun applyTo(abi: BundleAbi) {
        abi::enableSplit trySet enableSplit
    }
}
