package plugin.project.web.model

import kotlinx.serialization.Serializable

@Serializable
internal data class NodeSettings(
    val enabled: Boolean = true
)
