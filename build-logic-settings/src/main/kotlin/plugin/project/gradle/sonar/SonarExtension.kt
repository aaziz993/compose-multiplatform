package plugin.project.gradle.sonar

import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import gradle.sonar
import org.gradle.api.Project

// Project code analysis
// To analyze a project hierarchy, apply the SonarQube plugin to the root project of the hierarchy.
// Typically, (but not necessarily) this will be the root project of the Gradle build.
// Information pertaining to the analysis as a whole has to be configured in the sonar block of this project.
// Any properties set on the command line also apply to this project.
internal fun Project.configureSonarExtension() =
    pluginManager.withPlugin(settings.libs.plugins.plugin("sonarqube").id) {
        projectProperties.plugins.sonar.let { sonar ->
            sonar {
                sonar.applyTo(this)
            }
        }
    }
