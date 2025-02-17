package plugin.project.web.node.model

internal interface NodeJsRootExtension {
    val downloadBaseUrl: String?
    // To prevent Kotlin build from failing (due to `-Werror`), only deprecate after upgrade of bootstrap version
//    @Deprecated(
//        "Use downloadBaseUrl from NodeJsExtension (not NodeJsRootExtension) instead" +
//                "You can find this extension after applying NodeJsPlugin. This will be removed in 2.2"
//    )
    val version: String?
}
