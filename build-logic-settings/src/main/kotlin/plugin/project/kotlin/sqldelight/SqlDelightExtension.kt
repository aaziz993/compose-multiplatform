package plugin.project.kotlin.sqldelight

import app.cash.sqldelight.gradle.SqlDelightPlugin
import gradle.moduleProperties
import gradle.sqldelight
import gradle.tryAssign
import org.gradle.api.Project
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.withType

internal fun Project.configureSqlDelightExtension() =
    plugins.withType<SqlDelightPlugin> {
        moduleProperties.settings.kotlin.sqldelight.let { sqldelight ->
            sqldelight {
                sqldelight.applyTo(this)
            }
        }
    }
