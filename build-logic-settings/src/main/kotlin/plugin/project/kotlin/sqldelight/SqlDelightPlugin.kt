package plugin.project.kotlin.sqldelight

import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import gradle.sqldelight
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class SqlDelightPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.sqldelight
                .takeIf { it.enabled && projectProperties.kotlin.hasTargets }?.let { sqldelight ->
                    plugins.apply(settings.libs.plugins.plugin("sqldelight").id)

                    sqldelight.applyTo()
                }
        }
    }
}
