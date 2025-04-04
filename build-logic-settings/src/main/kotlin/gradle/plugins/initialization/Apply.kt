package gradle.plugins.initialization

import kotlinx.serialization.Serializable

@Serializable
internal data class Apply(
    val from: String? = null,
    val plugin: String? = null,
    val to: String? = null,
)
