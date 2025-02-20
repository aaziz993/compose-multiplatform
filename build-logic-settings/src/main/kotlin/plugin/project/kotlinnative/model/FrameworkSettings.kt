package plugin.project.kotlinnative.model

import kotlinx.serialization.Serializable

@Serializable
internal data class FrameworkSettings(
    val basename: String? = null,

    val isStatic: Boolean? = null,
)
