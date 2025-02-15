package plugin.project.compose.desktop.model

import kotlinx.serialization.Serializable

@Serializable
internal data class MacOsSettings(
    val iconFile: String = "jvmAppIcons/LinuxIcon.png",
    val bundleId: String? = null
)
