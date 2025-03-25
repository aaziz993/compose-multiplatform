package gradle.plugins.kmp.web

import gradle.api.tryAssign
import gradle.api.trySet
import gradle.plugins.web.node.NodeJsRootExtension
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsExec

@Serializable
internal data class NodeJsExec(
    val nodeJsRoot: NodeJsRootExtension? = null,
    val nodeArgs: List<String>? = null,
    val sourceMapStackTraces: Boolean? = null,
    val inputFileProperty: String? = null,
) {

    context(Project)
    fun applyTo(receiver: NodeJsExec) {
        nodeJsRoot?.applyTo(exec.nodeJsRoot)
        nodeArgs?.let(exec.nodeArgs::addAll)
        exec::sourceMapStackTraces trySet sourceMapStackTraces
        exec.inputFileProperty tryAssign inputFileProperty?.let(project::file)
    }
}
