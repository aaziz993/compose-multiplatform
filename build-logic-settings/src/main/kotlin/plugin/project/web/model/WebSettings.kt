package plugin.project.web.model

import kotlinx.serialization.Serializable
import plugin.project.web.js.karakum.model.KarakumSettings
import plugin.project.web.node.model.NodeSettings
import plugin.project.web.npm.model.NpmSettings
import plugin.project.web.yarn.model.YarnSettings

@Serializable
internal data class WebSettings(
    val node: NodeSettings = NodeSettings(),
    val yarn: YarnSettings = YarnSettings(),
    val npm: NpmSettings = NpmSettings(),
    val karakum: KarakumSettings = KarakumSettings(),
)
