package klib.data.database.mdb.query

public class JavaSelectQuery(
    public val selectQuery: com.healthmarketscience.jackcess.query.SelectQuery
) : SelectQuery, BaseSelectQuery by JavaBaseSelectQuery(selectQuery)
