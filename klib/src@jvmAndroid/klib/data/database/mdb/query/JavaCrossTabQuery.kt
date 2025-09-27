package klib.data.database.mdb.query

public class JavaCrossTabQuery(
    public val crossTabQuery: com.healthmarketscience.jackcess.query.CrossTabQuery
) : CrossTabQuery, BaseSelectQuery by JavaBaseSelectQuery(crossTabQuery) {
    override val transformExpression: String
        get() = crossTabQuery.transformExpression

    override val pivotExpression: String
        get() = crossTabQuery.pivotExpression
}