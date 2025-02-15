package plugin.project.web.model

import kotlinx.serialization.Serializable
import plugin.project.web.js.karakum.model.KarakumSettings

@Serializable
internal data class WebSettings(
    val moduleName: String? = null,
    val browser: BrowserSettings = BrowserSettings(),
    val node: NodeSettings = NodeSettings(),
    val karakum: KarakumSettings = KarakumSettings(),
    val executable: Boolean = false,
)
