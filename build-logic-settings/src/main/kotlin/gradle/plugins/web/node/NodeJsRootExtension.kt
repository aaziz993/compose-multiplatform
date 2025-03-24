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
internal class NodeJsRootExtension {

    context(project: Project)
    fun applyTo(receiver: NodeJsRootExtension) =
        project.pluginManager.withPlugin(project.settings.libs.plugins.plugin("gradle.node.plugin").id) {

        }

    context(project: Project)
    fun applyTA() = applyTo(project.node)
}
