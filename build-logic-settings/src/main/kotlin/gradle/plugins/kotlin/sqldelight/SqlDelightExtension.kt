package gradle.plugins.kotlin.sqldelight

import gradle.accessors.id
import gradle.accessors.catalog.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import gradle.accessors.sqldelight
import gradle.api.applyTo
import gradle.api.tryAssign
import org.gradle.api.Project

internal interface SqlDelightExtension {

    val databases: Set<SqlDelightDatabase>?
    val linkSqlite: Boolean?

    context(Project)
    fun applyTo() =
        project.pluginManager.withPlugin(project.settings.libs.plugin("sqldelight").id) {
            databases?.forEach { database ->
                database.applyTo(project.sqldelight.databases)
            }

            project.sqldelight.linkSqlite tryAssign linkSqlite
        }
}
