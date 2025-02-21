package plugin.project.apple.model

import kotlinx.serialization.Serializable

@Serializable
internal data class AppleSettings(
    override val teamID: String? = null,
    val ios: AppleTarget? = null,
) : AppleProjectExtension
