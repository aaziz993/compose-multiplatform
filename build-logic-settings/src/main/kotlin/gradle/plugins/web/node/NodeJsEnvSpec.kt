package gradle.plugins.web.node

import gradle.accessors.nodeEnv
import gradle.plugins.web.EnvSpec
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsEnvSpec

@Serializable
internal data class NodeJsEnvSpec(
    override val download: Boolean? = null,
    override val downloadBaseUrl: String? = null,
    override val installationDirectory: String? = null,
    override val version: String? = null,
    override val command: String? = null,
) : EnvSpec() {

    context(Project)
    fun applyTo() = super.applyTo(nodeEnv)
}
