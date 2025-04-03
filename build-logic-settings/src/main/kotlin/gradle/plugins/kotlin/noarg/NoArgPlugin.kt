package gradle.plugins.kotlin.noarg

import gradle.api.project.projectProperties
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class NoArgPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            // Apply noarg properties.
            projectProperties.noarg?.applyTo()
        }
    }
}
