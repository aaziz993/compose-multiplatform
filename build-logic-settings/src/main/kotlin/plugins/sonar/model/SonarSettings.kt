package plugins.sonar.model

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import gradle.plugins.sonar.SonarExtension
import gradle.project.EnabledSettings
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class SonarSettings(
    override val skipProject: Boolean? = null,
    override val properties: Map<String, String>? = null,
    override val androidVariant: String? = null,
    override val enabled: Boolean = true
) : SonarExtension, EnabledSettings {

    context(project: Project)
    override fun applyTo() =
        project.pluginManager.withPlugin(project.settings.libs.plugins.plugin("sonarqube").id) {
            super.applyTo()
        }
}
