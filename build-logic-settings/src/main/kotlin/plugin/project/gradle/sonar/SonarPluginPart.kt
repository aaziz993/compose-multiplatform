package plugin.project.gradle.sonar

import gradle.moduleProperties
import gradle.libs
import plugin.project.BindingPluginPart
import org.gradle.api.Project

internal class SonarPluginPart(override val project: Project) : BindingPluginPart {

    override val needToApply: Boolean by lazy {
        project.moduleProperties.settings.gradle.sonar.enabled
    }

    override fun applyAfterEvaluate() = with(project) {
        plugins.apply(project.libs.plugins.sonarqube.get().pluginId)

        applySettings()
    }

    fun applySettings() {
        with(project) {
            configureSonarExtension()
        }
    }
}
