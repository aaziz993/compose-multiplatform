package plugins.kotlin.sqldelight

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import gradle.accessors.settings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class SqlDelightPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.sqldelight
                .takeIf { it.enabled && projectProperties.kotlin.targets.isNotEmpty() }?.let { sqldelight ->
                    plugins.apply(settings.libs.plugins.plugin("sqldelight").id)

                    sqldelight.applyTo()
                }
        }
    }
}
