package gradle.plugins.kotlin.targets.web

import gradle.api.file.tryAssign
import gradle.collection.tryAddAll
import gradle.plugins.kotlin.targets.web.node.NodeJsRootExtension
import klib.data.type.reflection.trySet
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
        nodeJsRoot?.applyTo(receiver.nodeJsRoot)
        receiver.nodeArgs tryAddAll nodeArgs
        receiver::sourceMapStackTraces trySet sourceMapStackTraces
        receiver.inputFileProperty tryAssign inputFileProperty?.let(project::file)
    }
}
