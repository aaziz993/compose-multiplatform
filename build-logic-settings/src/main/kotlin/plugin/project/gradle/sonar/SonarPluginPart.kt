package plugin.project.gradle.sonar

import gradle.libs
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class SonarPluginPart : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        if (!settings.projectProperties.plugins.sonar.enabled || settings.projectProperties.kotlin.targets.isEmpty()) {
            return@with
        }

        plugins.apply(project.libs.plugins.sonarqube.get().pluginId)

        configureSonarExtension()
    }
}
