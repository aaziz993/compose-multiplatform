package plugin.project.kotlin.sqldelight

import app.cash.sqldelight.gradle.SqlDelightPlugin
import gradle.projectProperties
import gradle.settings
import gradle.sqldelight
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

internal fun Project.configureSqlDelightExtension() =
    plugins.withType<SqlDelightPlugin> {
       settings.projectProperties.plugins.sqldelight.let { sqldelight ->
            sqldelight {
                sqldelight.applyTo(this)
            }
        }
    }
