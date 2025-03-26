package gradle.plugins.sonar.model

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import gradle.plugins.sonar.SonarExtension
import gradle.plugins.sonar.SonarProperties
import gradle.plugins.project.EnabledSettings
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class SonarSettings(
    override val skipProject: Boolean? = null,
    override val properties: SonarProperties? = null,
    override val androidVariant: String? = null,
    override val enabled: Boolean = true
) : SonarExtension, EnabledSettings {

    context(Project)
    override fun applyTo() =
        project.pluginManager.withPlugin(project.settings.libs.plugins.plugin("sonarqube").id) {
            super.applyTo()
        }
}
