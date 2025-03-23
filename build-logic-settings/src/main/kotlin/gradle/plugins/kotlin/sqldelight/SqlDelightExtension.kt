package gradle.plugins.kotlin.sqldelight

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import gradle.accessors.sqldelight
import gradle.api.applyTo
import gradle.api.tryAssign
import org.gradle.api.Project
import org.gradle.kotlin.dsl.invoke

internal interface SqlDelightExtension {

    val databases: Set<SqlDelightDatabase>?
    val linkSqlite: Boolean?

    context(Project)
    fun applyTo() =
        pluginManager.withPlugin(settings.libs.plugins.plugin("sqldelight").id) {
            databases?.forEach { database ->
                database.applyTo(sqldelight.databases)
            }

            sqldelight.linkSqlite tryAssign linkSqlite
        }
}
