package klib.data.database.mdb.query

public class JavaDeleteQuery(
    public val deleteQuery: com.healthmarketscience.jackcess.query.DeleteQuery
) : DeleteQuery, BaseSelectQuery by JavaBaseSelectQuery(deleteQuery)
