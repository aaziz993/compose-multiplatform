package gradle.plugins.buildconfig

import gradle.api.project.projectProperties
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class BuildConfigPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            // Apply buildconfig properties.
            projectProperties.buildconfig?.applyTo()
        }
    }
}
