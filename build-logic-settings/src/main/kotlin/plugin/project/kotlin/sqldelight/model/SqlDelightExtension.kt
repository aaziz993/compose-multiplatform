package plugin.project.kotlin.sqldelight.model

import gradle.tryAssign
import org.gradle.api.Project
import org.gradle.kotlin.dsl.invoke

internal interface SqlDelightExtension {

    val databases: List<SqlDelightDatabase>?
    val linkSqlite: Boolean?

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
