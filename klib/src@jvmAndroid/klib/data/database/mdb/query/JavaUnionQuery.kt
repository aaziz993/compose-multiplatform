package klib.data.database.mdb.query

public class JavaUnionQuery(
    public val unionQuery: com.healthmarketscience.jackcess.query.UnionQuery
) : UnionQuery, Query by JavaQuery(unionQuery) {
    override val unionType: String
        get() = unionQuery.unionType

    override val unionString1: String
        get() = unionQuery.unionString1

    override val unionString2: String
        get() = unionQuery.unionString2

    override val orderings: List<String>
        get() = unionQuery.orderings
}
