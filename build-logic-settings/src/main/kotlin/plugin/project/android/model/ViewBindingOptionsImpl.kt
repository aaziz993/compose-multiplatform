package plugin.project.android.model

import kotlinx.serialization.Serializable

/** DSL object for configuring view binding options.  */
@Serializable
internal data class ViewBindingOptions(
    val enable: Boolean? = null,
)
