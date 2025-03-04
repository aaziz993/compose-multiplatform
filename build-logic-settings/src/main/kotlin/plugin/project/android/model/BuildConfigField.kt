package plugin.project.android.model

import kotlinx.serialization.Serializable

@Serializable
internal data class BuildConfigField(
   val type: String,
   val name: String,
   val value: String
)
