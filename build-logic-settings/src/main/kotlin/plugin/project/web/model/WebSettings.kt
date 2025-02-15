package plugin.project.web.model

import kotlinx.serialization.Serializable

@Serializable
internal data class WebSettings(
    val framework: WebFrameworkSettings = WebFrameworkSettings()
)
