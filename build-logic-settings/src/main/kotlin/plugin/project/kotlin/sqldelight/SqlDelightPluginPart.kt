package plugin.project.kotlin.sqldelight

import gradle.libs
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class SqlDelightPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (projectProperties.plugins.sqldelight.enabled || projectProperties.kotlin.hasTargets) {
                return@with
            }

            plugins.apply(project.libs.plugins.sqldelight.get().pluginId)

            configureSqlDelightExtension()
        }
    }
}
