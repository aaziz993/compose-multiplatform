package plugin.project.model

import kotlinx.serialization.Serializable
import plugin.project.compose.model.ComposeSettings
import plugin.project.gradlle.model.GradleSettings
import plugin.project.web.model.WebSettings

@Serializable
internal data class Settings(
    val gradle: GradleSettings = GradleSettings(),
    val web: WebSettings = WebSettings(),
    val compose: ComposeSettings = ComposeSettings()
)
