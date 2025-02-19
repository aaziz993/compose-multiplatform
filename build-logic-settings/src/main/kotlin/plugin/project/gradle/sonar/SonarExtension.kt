package plugin.project.gradle.sonar

import gradle.moduleProperties
import gradle.sonar
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.sonarqube.gradle.SonarQubePlugin

// Project code analysis
// To analyze a project hierarchy, apply the SonarQube plugin to the root project of the hierarchy.
// Typically, (but not necessarily) this will be the root project of the Gradle build.
// Information pertaining to the analysis as a whole has to be configured in the sonar block of this project.
// Any properties set on the command line also apply to this project.
internal fun Project.configureSonarExtension() =
    plugins.withType<SonarQubePlugin> {
        moduleProperties.settings.gradle.sonar.let { sonar ->
            sonar {
                sonar.skipProject?.let(::setSkipProject)
                properties {
                    property("sonar.projectVersion", version)
                }
                sonar.properties?.let { properties ->
                    properties {
                        properties.forEach { (key, value) -> property(key, value) }
                    }
                }
                sonar.androidVariant?.let(::setAndroidVariant)
            }
        }
    }
