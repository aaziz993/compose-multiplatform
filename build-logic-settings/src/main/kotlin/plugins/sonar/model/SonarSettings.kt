package plugins.sonar.model

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import gradle.project.EnabledSettings
import gradle.plugins.sonar.SonarExtension
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class SonarSettings(
    override val skipProject: Boolean? = null,
    override val properties: Map<String, String>? = null,
    override val androidVariant: String? = null,
    override val enabled: Boolean = true
) : SonarExtension, EnabledSettings {

    context(Project)
    override fun applyTo() =
        pluginManager.withPlugin(settings.libs.plugins.plugin("sonarqube").id) {
            super.applyTo()
        }
}
