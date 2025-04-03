package gradle.plugins.sonar

import gradle.api.project.projectProperties
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class SonarPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            // Apply sonar properties.
            projectProperties.sonar?.applyTo()
        }
    }
}
