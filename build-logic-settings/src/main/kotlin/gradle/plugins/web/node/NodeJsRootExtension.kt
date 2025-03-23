package gradle.plugins.web.node

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.node
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import gradle.api.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension

@Serializable
internal data class NodeJsRootExtension(
    val downloadBaseUrl: String? = null,
    val version: String? = null,
) {

    context(Project)
    fun applyTo(receiver: NodeJsRootExtension) =
        pluginManager.withPlugin(settings.libs.plugins.plugin("gradle.node.plugin").id) {
            receiver::downloadBaseUrl trySet downloadBaseUrl
            receiver::version trySet version
        }

    context(Project)
    fun applyTA() = applyTo(node)
}
