package klib.data.database.mdb.query

public class JavaPassthroughQuery(
    public val passthroughQuery: com.healthmarketscience.jackcess.query.PassthroughQuery
) : PassthroughQuery, Query by JavaQuery(passthroughQuery) {
    override val connectionString: String
        get() = passthroughQuery.connectionString

    override val passthroughString: String
        get() = passthroughQuery.passthroughString
}