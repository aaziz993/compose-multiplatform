package plugin.project.compose.desktop.model

import kotlinx.serialization.Serializable
import org.jetbrains.compose.desktop.DesktopExtension
import plugin.project.compose.desktop.model.linux.LinuxSettings
import plugin.project.compose.desktop.model.macos.MacOsSettings
import plugin.project.compose.desktop.model.windows.WindowsSettings

@Serializable
internal data class DesktopSettings(
    val packageName: String? = null,
    val packageVersion: String? = null,
    val linux: LinuxSettings = LinuxSettings(),
    val windows: WindowsSettings = WindowsSettings(),
    val macOs: MacOsSettings = MacOsSettings(),
){
    fun applyTo(extension: DesktopExtension){

    }
}
