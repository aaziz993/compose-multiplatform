package plugin.project.kotlin.model.language.web

import kotlinx.serialization.Serializable
import plugin.project.web.node.model.NodeJsRootExtension

@Serializable
internal data class NodeJsExec(
    val nodeJsRoot: NodeJsRootExtension? = null,
    val nodeArgs: List<String>,
    val sourceMapStackTraces: Boolean? = null,
    val inputFileProperty: String? = null,
)
