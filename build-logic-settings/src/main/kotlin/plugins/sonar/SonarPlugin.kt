package plugins.sonar

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import gradle.accessors.settings
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
