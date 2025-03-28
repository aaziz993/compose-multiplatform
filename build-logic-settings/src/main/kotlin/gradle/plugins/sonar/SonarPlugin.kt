package gradle.plugins.sonar

import gradle.accessors.id
import gradle.accessors.catalog.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import gradle.accessors.settings
import gradle.plugins.sonar.model.SonarSettings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class SonarPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.sonar
                .takeIf(SonarSettings::enabled)?.let { sonar ->
                    plugins.apply(project.settings.libs.plugin("sonarqube").id)

                    sonar.applyTo()
                }
        }
    }
}
