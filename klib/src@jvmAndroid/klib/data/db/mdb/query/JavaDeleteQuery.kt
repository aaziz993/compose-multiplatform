package klib.data.db.mdb.query

public class JavaDeleteQuery(
    public val deleteQuery: com.healthmarketscience.jackcess.query.DeleteQuery
) : DeleteQuery, BaseSelectQuery by JavaBaseSelectQuery(deleteQuery)
