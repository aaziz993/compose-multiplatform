package plugin.project.compose.desktop.model.windows

import kotlinx.serialization.Serializable

@Serializable
internal data class WindowsSettings(
    val iconFile: String = "jvmAppIcons/LinuxIcon.png"
)
