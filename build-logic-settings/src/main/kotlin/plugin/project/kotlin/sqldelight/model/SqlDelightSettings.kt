package plugin.project.kotlin.sqldelight.model

import gradle.tryAssign
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.invoke

@Serializable
internal data class SqlDelightSettings(
    override val databases: List<SqlDelightDatabase>? = null,
    override val linkSqlite: Boolean? = null,
    val enabled: Boolean = true
) : SqlDelightExtension {

    context(Project)
    fun applyTo(extension: app.cash.sqldelight.gradle.SqlDelightExtension) {
        databases?.let { databases ->
            extension.databases {
                databases.forEach { database ->
                    //Note: Name of your Database and .sq file should be same
                    create(database.name) {
                        database.applyTo(this)
                    }
                }
            }
        }
        extension.linkSqlite tryAssign linkSqlite
    }
}
