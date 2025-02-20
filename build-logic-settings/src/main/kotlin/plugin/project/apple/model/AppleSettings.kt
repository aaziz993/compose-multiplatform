package plugin.project.apple.model

import kotlinx.serialization.Serializable

@Serializable
internal data class AppleSettings(
    val teamId: String? = null,
)
