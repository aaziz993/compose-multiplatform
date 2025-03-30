package gradle.plugins.kotlin.sqldelight

import gradle.accessors.projectProperties
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class SqlDelightPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            // Apply sqldelight properties.
            projectProperties.sqldelight?.applyTo()
        }
    }
}
