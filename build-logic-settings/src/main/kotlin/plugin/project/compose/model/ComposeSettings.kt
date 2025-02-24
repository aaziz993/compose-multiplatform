package plugin.project.compose.model

import kotlinx.serialization.Serializable
import plugin.project.compose.desktop.model.DesktopExtension
import plugin.project.compose.resources.model.ResourcesExtension
import plugin.project.model.EnabledSettings

@Serializable
internal data class ComposeSettings(
    val resources: ResourcesExtension = ResourcesExtension(),
    val desktop: DesktopExtension = DesktopExtension(),
    override val enabled: Boolean = false
) : EnabledSettings
