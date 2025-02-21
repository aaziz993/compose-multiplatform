package plugin.project.gradle.sonar

import gradle.libs
import gradle.moduleProperties
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class SonarPluginPart : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        if (!moduleProperties.settings.gradle.sonar.enabled || moduleProperties.targets.isEmpty()) {
            return@with
        }

        plugins.apply(project.libs.plugins.sonarqube.get().pluginId)

        configureSonarExtension()
    }
}
