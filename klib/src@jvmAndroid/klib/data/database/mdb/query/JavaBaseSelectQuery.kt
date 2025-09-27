package klib.data.database.mdb.query


internal class JavaBaseSelectQuery(private val query: com.healthmarketscience.jackcess.query.BaseSelectQuery) :
    BaseSelectQuery, Query by JavaQuery(query) {
    override val selectType: String
        get() = query.selectType

    override val selectColumns: List<String>
        get() = query.selectColumns

    override val fromTables: List<String>
        get() = query.fromTables

    override val fromRemoteDbPath: String
        get() = query.fromRemoteDbPath

    override val fromRemoteDbType: String
        get() = query.fromRemoteDbType

    override val whereExpression: String
        get() = query.whereExpression

    override val groupings: List<String>
        get() = query.groupings

    override val havingExpression: String
        get() = query.havingExpression

    override val orderings: List<String>
        get() = query.orderings
}