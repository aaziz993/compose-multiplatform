package plugin.project.compose.desktop.model

import kotlinx.serialization.Serializable

@Serializable
internal data class LinuxSettings(
    val iconFile: String = "jvmAppIcons/LinuxIcon.png"
)
