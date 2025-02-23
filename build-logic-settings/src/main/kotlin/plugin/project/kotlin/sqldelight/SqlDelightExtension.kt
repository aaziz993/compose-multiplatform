package plugin.project.kotlin.sqldelight

import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import app.cash.sqldelight.gradle.SqlDelightPlugin
import gradle.projectProperties
import gradle.settings
import gradle.sqldelight
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

internal fun Project.configureSqlDelightExtension() =
    pluginManager.withPlugin(settings.libs.plugins.plugin("sqldelight").id) {
        projectProperties.plugins.sqldelight.let { sqldelight ->
            sqldelight {
                sqldelight.applyTo(this)
            }
        }
    }
