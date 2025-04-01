package gradle.plugins.android.features

import com.android.build.api.dsl.ViewBinding
import klib.data.type.reflection.trySet
import kotlinx.serialization.Serializable

/**
 * DSL object for configuring viewbinding options.
 */
@Serializable
internal data class ViewBinding(
    /** Whether to enable view binding. */
    val enable: Boolean? = null,
) {

    fun applyTo(receiver: ViewBinding) {
        receiver::enable trySet enable
    }
}
