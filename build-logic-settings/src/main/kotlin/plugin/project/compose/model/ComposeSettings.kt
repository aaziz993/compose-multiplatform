package plugin.project.compose.model

import kotlinx.serialization.Serializable
import plugin.project.compose.desktop.model.DesktopExtension
import plugin.project.compose.resources.model.ResourcesExtension

@Serializable
internal data class ComposeSettings(
    val resources: ResourcesExtension = ResourcesExtension(),
    val desktop: DesktopExtension = DesktopExtension(),
    val enabled: Boolean = false
)
