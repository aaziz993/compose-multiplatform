package plugins.kotlin.sqldelight

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import gradle.accessors.settings
import org.gradle.api.Plugin
import org.gradle.api.Project
import plugins.kotlin.sqldelight.model.SqlDelightSettings

internal class SqlDelightPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.sqldelight
                .takeIf(SqlDelightSettings::enabled)?.let { sqldelight ->
                    plugins.apply(project.settings.libs.plugins.plugin("sqldelight").id)

                    sqldelight.applyTo()
                }
        }
    }
}
