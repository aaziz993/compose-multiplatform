package gradle.plugins.web

import gradle.api.tryAssign
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.EnvSpec

/**
 * Instance which describes specific runtimes for JS and Wasm targets
 *
 * It encapsulates necessary information about a tool to run application and tests
 */
internal abstract class EnvSpec {

    /**
     * Specify whether we need to download the tool
     */
    abstract val download: Boolean?

    /**
     * Specify url to add repository from which the tool is going to be downloaded
     *
     * If the property has no value, repository is not added,
     * so this can be used to add your own repository where the tool is located
     */
    abstract val downloadBaseUrl: String?

    /**
     * Specify where the tool is installed
     */
    abstract val installationDirectory: String?

    /**
     * Specify a version of the tool is installed
     */
    abstract val version: String?

    /**
     * Specify a command to run the tool
     */
    abstract val command: String?

    context(project: Project)
    open fun applyTo(receiver: EnvSpec<*>) {
        receiver.download tryAssign download
        receiver.downloadBaseUrl tryAssign downloadBaseUrl
        receiver.installationDirectory tryAssign installationDirectory?.let(project.layout.projectDirectory::dir)
        receiver.version tryAssign version
        receiver.command tryAssign command
    }
}
