package gradle.plugins.kotlin.sqldelight

import gradle.accessors.sqldelight
import gradle.api.applyTo
import gradle.api.tryAssign
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class SqlDelightExtension(
    val databases: Set<SqlDelightDatabase>? = null,
    val linkSqlite: Boolean? = null,
) {

    context(Project)
    fun applyTo() =
        project.pluginManager.withPlugin("app.cash.sqldelight") {
            databases?.forEach { database ->
                database.applyTo(project.sqldelight.databases)
            }

            project.sqldelight.linkSqlite tryAssign linkSqlite
        }
}
