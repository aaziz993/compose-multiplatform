package gradle.plugins.kotlin.allopen

import gradle.accessors.projectProperties
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class AllOpenPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            // Apply allOpen properties.
            projectProperties.allOpen?.applyTo()
        }
    }
}
