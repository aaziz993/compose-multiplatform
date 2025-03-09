package gradle.model.android

import com.android.build.api.dsl.ViewBinding
import gradle.trySet
import kotlinx.serialization.Serializable

/**
 * DSL object for configuring viewbinding options.
 */
@Serializable
internal data class ViewBinding(
    /** Whether to enable view binding. */
    val enable: Boolean? = null,
) {

    fun applyTo(binding: ViewBinding) {
        binding::enable trySet enable
    }
}
