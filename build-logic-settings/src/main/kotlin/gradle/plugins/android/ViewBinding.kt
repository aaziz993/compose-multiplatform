package gradle.plugins.android

import com.android.build.api.dsl.ViewBinding
import gradle.api.trySet
import kotlinx.serialization.Serializable

/**
 * DSL object for configuring viewbinding options.
 */
@Serializable
internal data class ViewBinding(
    /** Whether to enable view binding. */
    val enable: Boolean? = null,
) {

    fun applyTo(recipient: ViewBinding) {
        recipient::enable trySet enable
    }
}
