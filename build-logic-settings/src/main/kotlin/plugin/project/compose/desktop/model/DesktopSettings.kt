package plugin.project.compose.desktop.model

import kotlinx.serialization.Serializable

@Serializable
internal data class DesktopSettings(
    val packageName: String? = null,
    val packageVersion: String? = null,
    val linux: LinuxSettings = LinuxSettings(),
    val windows: WindowsSettings = WindowsSettings(),
    val macOs: MacOsSettings = MacOsSettings(),
)
