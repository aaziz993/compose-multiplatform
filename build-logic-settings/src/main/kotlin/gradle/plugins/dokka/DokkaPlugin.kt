package gradle.plugins.dokka

import gradle.api.project.projectProperties
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class DokkaPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            // Apply dokka properties.
            projectProperties.dokka?.applyTo()
        }
    }
}
