package plugin.project.web.model

import kotlinx.serialization.Serializable
import plugin.project.web.js.karakum.model.KarakumSettings

@Serializable
internal data class WebFrameworkSettings(
    val moduleName: String? = null,
    val karakum: KarakumSettings = KarakumSettings()
)
