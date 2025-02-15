package plugin.project.web.model

import kotlinx.serialization.Serializable

@Serializable
internal data class BrowserSettings(
    val enabled: Boolean = true
)
