package plugin.project.kotlin.serialization.model

import kotlinx.serialization.Serializable

@Serializable
internal data class SerializationSettings(
    val enabled: Boolean = true
)
