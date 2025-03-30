package gradle.plugins.kotlin.noarg

import gradle.accessors.projectProperties
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class NoArgPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            // Apply noArg properties.
            projectProperties.noArg?.applyTo()
        }
    }
}
