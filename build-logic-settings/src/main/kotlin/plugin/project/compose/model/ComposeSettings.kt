package plugin.project.compose.model

import kotlinx.serialization.Serializable
import plugin.project.compose.desktop.model.DesktopSettings

@Serializable
internal data class ComposeSettings(
    val resources: ResourcesExtension = ResourcesExtension(),
    val desktop: DesktopSettings = DesktopSettings(),
    val enabled: Boolean = false
)
