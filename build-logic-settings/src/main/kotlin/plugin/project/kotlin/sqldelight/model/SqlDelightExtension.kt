package plugin.project.kotlin.sqldelight.model

internal interface SqlDelightExtension {

    val databases: List<SqlDelightDatabase>
    val linkSqlite: Boolean?
}
