package klib.data.database.mdb.query

public class JavaUpdateQuery(
    public val updateQuery: com.healthmarketscience.jackcess.query.UpdateQuery
) : UpdateQuery, Query by JavaQuery(updateQuery) {
    override val targetTables: List<String>
        get() = updateQuery.targetTables

    override val remoteDbPath: String
        get() = updateQuery.remoteDbPath

    override val remoteDbType: String
        get() = updateQuery.remoteDbType

    override val newValues: List<String>
        get() = updateQuery.newValues

    override val whereExpression: String
        get() = updateQuery.whereExpression
}