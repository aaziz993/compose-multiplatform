package gradle.plugins.kotlin.targets.web.node

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.nodeEnv
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import gradle.plugins.kotlin.targets.web.EnvSpec
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class NodeJsEnvSpec(
    override val download: Boolean? = null,
    override val downloadBaseUrl: String? = null,
    override val installationDirectory: String? = null,
    override val version: String? = null,
    override val command: String? = null,
) : EnvSpec() {

    context(Project)
    fun applyTo() =
        project.pluginManager.withPlugin(project.settings.libs.plugins.plugin("gradle.node.plugin").id) {
            super.applyTo(project.nodeEnv)
        }
}
