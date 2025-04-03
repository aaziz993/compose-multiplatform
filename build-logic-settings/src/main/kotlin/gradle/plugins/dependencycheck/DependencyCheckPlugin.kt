package gradle.plugins.dependencycheck

import gradle.api.project.projectProperties
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class DependencyCheckPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            // Apply dependencyCheck properties.
            projectProperties.dependencyCheck?.applyTo()
        }
    }
}
