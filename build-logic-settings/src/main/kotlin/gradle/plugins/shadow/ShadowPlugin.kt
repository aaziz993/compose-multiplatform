package gradle.plugins.shadow

import gradle.api.project.projectProperties
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class ShadowPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            // Apply shadow properties.
            projectProperties.shadow?.applyTo()
        }
    }
}
