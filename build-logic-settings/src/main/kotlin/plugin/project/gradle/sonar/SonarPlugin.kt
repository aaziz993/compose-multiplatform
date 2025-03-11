package plugin.project.gradle.sonar

import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class SonarPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.sonar
                .takeIf { it.enabled && projectProperties.kotlin.targets.isNotEmpty() }?.let { sonar ->
                    plugins.apply(settings.libs.plugins.plugin("sonarqube").id)

                    sonar.applyTo()
                }
        }
    }
}
