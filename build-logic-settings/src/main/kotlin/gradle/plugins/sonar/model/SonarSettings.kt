package gradle.plugins.sonar.model

import gradle.accessors.catalog.libs

import gradle.accessors.settings
import gradle.plugins.project.EnabledSettings
import gradle.plugins.sonar.SonarExtension
import gradle.plugins.sonar.SonarProperties
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
        project.pluginManager.withPlugin(project.settings.libs.plugin("sonarqube").id) {
            super.applyTo()
        }
}
