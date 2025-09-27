package klib.data.database.mdb.query

public class JavaMakeTableQuery(
    public val makeTableQuery: com.healthmarketscience.jackcess.query.MakeTableQuery
) : MakeTableQuery, BaseSelectQuery by JavaBaseSelectQuery(makeTableQuery) {
    override val targetTable: String
        get() = makeTableQuery.targetTable

    override val remoteDbPath: String
        get() = makeTableQuery.remoteDbPath

    override val remoteDbType: String
        get() = makeTableQuery.remoteDbType
}