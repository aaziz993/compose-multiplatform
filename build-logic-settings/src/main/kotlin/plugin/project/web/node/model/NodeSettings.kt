package plugin.project.web.node.model

import kotlinx.serialization.Serializable

@Serializable
internal data class NodeSettings(
    override val downloadBaseUrl: String? = null,
// To prevent Kotlin build from failing (due to `-Werror`), only deprecate after upgrade of bootstrap version
//    @Deprecated(
//        "Use downloadBaseUrl from NodeJsExtension (not NodeJsRootExtension) instead" +
//                "You can find this extension after applying NodeJsPlugin. This will be removed in 2.2"
//    )
    override val version: String? = null,
    val env: EnvSpec = EnvSpec(),
) : NodeJsRootExtension
