package plugin.project.android.model

import kotlinx.serialization.Serializable

@Serializable
internal data class ResValue(
    val type: String,
    val name: String,
    val value: String
)
