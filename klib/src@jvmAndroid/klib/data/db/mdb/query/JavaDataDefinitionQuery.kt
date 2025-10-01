package klib.data.db.mdb.query

public class JavaDataDefinitionQuery(
    public val dataDefinitionQuery: com.healthmarketscience.jackcess.query.DataDefinitionQuery
) : DataDefinitionQuery, Query by JavaQuery(dataDefinitionQuery) {
    override val ddLString: String
        get() = dataDefinitionQuery.ddlString
}
