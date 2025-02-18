package plugin.project.kotlin.sqldelight

import app.cash.sqldelight.gradle.SqlDelightPlugin
import gradle.amperModuleExtraProperties
import gradle.sqldelight
import gradle.tryAssign
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

internal fun Project.configureSqlDelightExtension() =
    plugins.withType<SqlDelightPlugin> {
        amperModuleExtraProperties.settings.kotlin.sqldelight.let { sqldelight ->
            sqldelight {
                linkSqlite tryAssign sqldelight.linkSqlite
            }
        }
    }
