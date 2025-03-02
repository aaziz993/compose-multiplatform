package plugin.project.gradle.sonar.model

import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.settings
import gradle.sonar
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import plugin.project.model.EnabledSettings

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
