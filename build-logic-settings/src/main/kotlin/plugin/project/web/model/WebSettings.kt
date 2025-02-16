package plugin.project.web.model

import kotlinx.serialization.Serializable
import plugin.project.web.js.karakum.model.KarakumSettings
import plugin.project.web.node.model.NodeSettings
import plugin.project.web.npm.model.NpmSettings
import plugin.project.web.yarn.YarnSettings

@Serializable
internal data class WebSettings(
    val moduleName: String? = null,
    val browser: BrowserSettings = BrowserSettings(),
    val node: NodeSettings = NodeSettings(),
    val yarn: YarnSettings = YarnSettings(),
    val npm: NpmSettings = NpmSettings(),
    val karakum: KarakumSettings = KarakumSettings(),
    val executable: Boolean = false,
)
