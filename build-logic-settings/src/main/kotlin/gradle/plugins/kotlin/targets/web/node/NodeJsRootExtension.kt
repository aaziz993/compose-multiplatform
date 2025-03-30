package gradle.plugins.kotlin.targets.web.node

import gradle.accessors.node
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension

@Serializable
internal class NodeJsRootExtension {

    context(Project)
    fun applyTo(receiver: NodeJsRootExtension) =
        project.pluginManager.withPlugin("com.github.node-gradle.node") {

        }

    context(Project)
    fun applyTA() = applyTo(project.node)
}
