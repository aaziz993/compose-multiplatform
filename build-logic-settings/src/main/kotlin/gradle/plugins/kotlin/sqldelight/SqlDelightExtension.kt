package gradle.plugins.kotlin.sqldelight

import gradle.accessors.sqldelight
import gradle.api.tryAssign
import org.gradle.api.Project
import org.gradle.kotlin.dsl.invoke

internal interface SqlDelightExtension {

    val databases: List<SqlDelightDatabase>?
    val linkSqlite: Boolean?

    context(Project)
    fun applyTo() {
        databases?.let { databases ->
            sqldelight.databases {
                databases.forEach { database ->
                    //Note: Name of your Database and .sq file should be same
                    create(database.name) {
                        database.applyTo(this)
                    }
                }
            }
        }
        sqldelight.linkSqlite tryAssign linkSqlite
    }
}
