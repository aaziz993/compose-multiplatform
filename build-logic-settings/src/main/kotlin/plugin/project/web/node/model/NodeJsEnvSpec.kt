package plugin.project.web.node.model

import gradle.tryAssign
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsEnvSpec

@Serializable
internal data class NodeJsEnvSpec(
    /**
     * Specify whether we need to download the tool
     */
    val download: Boolean? = null,
    /**
     * Specify url to add repository from which the tool is going to be downloaded
     *
     * If the property has no value, repository is not added,
     * so this can be used to add your own repository where the tool is located
     */
    val downloadBaseUrl: String? = null,
    /**
     * Specify where the tool is installed
     */
    val installationDirectory: String? = null,
    /**
     * Specify a version of the tool is installed
     */
    val version: String? = null,
    /**
     * Specify a command to run the tool
     */
    val command: String? = null
) {

    context(Project)
    fun applyTo(spec: NodeJsEnvSpec) {
        spec.download tryAssign download
        spec.downloadBaseUrl tryAssign downloadBaseUrl
        spec.installationDirectory tryAssign installationDirectory?.let(layout.projectDirectory::dir)
        spec.version tryAssign version
        spec.command tryAssign command
    }
}
