package gradle.plugins.kotlin.targets.web.node

import gradle.accessors.catalog.libs
import gradle.accessors.node

import gradle.accessors.settings
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension

@Serializable
internal class NodeJsRootExtension {

    context(Project)
    fun applyTo(receiver: NodeJsRootExtension) =
        project.pluginManager.withPlugin(project.settings.libs.plugin("gradle.node.plugin").id) {

        }

    context(Project)
    fun applyTA() = applyTo(project.node)
}
