package gradle.plugins.kotlin.powerassert

import gradle.accessors.projectProperties
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class PowerAssertPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            // Apply powerAssert properties.
            projectProperties.powerAssert?.applyTo()
        }
    }
}
