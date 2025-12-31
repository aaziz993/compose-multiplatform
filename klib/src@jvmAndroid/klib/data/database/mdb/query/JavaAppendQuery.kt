package klib.data.database.mdb.query

public class JavaAppendQuery(
    public val appendQuery: com.healthmarketscience.jackcess.query.AppendQuery
) : AppendQuery, BaseSelectQuery by JavaBaseSelectQuery(appendQuery) {
    override val targetTable: String
        get() = appendQuery.targetTable

    override val targetColumns: List<String>
        get() = appendQuery.targetColumns

    override val remoteDbPath: String
        get() = appendQuery.remoteDbPath

    override val remoteDbType: String
        get() = appendQuery.remoteDbType

    override val values: List<String>
        get() = appendQuery.values
}
