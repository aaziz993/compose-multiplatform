package plugin.project.ios.model

import kotlinx.serialization.Serializable

@Serializable
internal data class IosSettings(
    val teamId: String? = null,
)
