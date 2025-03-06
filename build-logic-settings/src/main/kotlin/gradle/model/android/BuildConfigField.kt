package gradle.model.android

import kotlinx.serialization.Serializable

@Serializable
internal data class BuildConfigField(
   val type: String,
   val name: String,
   val value: String
)
