package plugin.project.gradle.sonar

import gradle.libs
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class SonarPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        if (!projectProperties.plugins.sonar.enabled || projectProperties.kotlin.hasTargets) {
            return@with
        }

        plugins.apply(project.libs.plugins.sonarqube.get().pluginId)

        configureSonarExtension()
    }
}
