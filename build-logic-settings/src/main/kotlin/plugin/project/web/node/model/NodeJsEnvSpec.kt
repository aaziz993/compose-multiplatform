package plugin.project.web.node.model

import gradle.id
import gradle.libs
import gradle.nodeEnv
import gradle.plugin
import gradle.plugins
import gradle.settings
import gradle.tryAssign
import kotlinx.serialization.Serializable
import org.gradle.api.Project

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
    fun applyTo() = pluginManager.withPlugin(settings.libs.plugins.plugin("gradle.node.plugin").id) {
        nodeEnv.download tryAssign download
        nodeEnv.downloadBaseUrl tryAssign downloadBaseUrl
        nodeEnv.installationDirectory tryAssign installationDirectory?.let(layout.projectDirectory::dir)
        nodeEnv.version tryAssign version
        nodeEnv.command tryAssign command
    }
}
