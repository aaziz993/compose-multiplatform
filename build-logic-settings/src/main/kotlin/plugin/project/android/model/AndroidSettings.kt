package plugin.project.android.model

import kotlinx.serialization.Serializable

@Serializable
internal data class AndroidSettings(
    val namespace: String? = null
)
