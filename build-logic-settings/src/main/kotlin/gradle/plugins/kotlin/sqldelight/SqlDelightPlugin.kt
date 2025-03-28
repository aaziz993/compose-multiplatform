package gradle.plugins.kotlin.sqldelight

import gradle.accessors.id
import gradle.accessors.catalog.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import gradle.accessors.settings
import gradle.plugins.kotlin.sqldelight.model.SqlDelightSettings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class SqlDelightPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.sqldelight
                .takeIf(SqlDelightSettings::enabled)?.let { sqldelight ->
                    plugins.apply(project.settings.libs.plugin("sqldelight").id)

                    sqldelight.applyTo()
                }
        }
    }
}
