package plugin.project.model

import kotlinx.serialization.Serializable

@Serializable
internal data class Properties(
    val settings: Settings,
)
