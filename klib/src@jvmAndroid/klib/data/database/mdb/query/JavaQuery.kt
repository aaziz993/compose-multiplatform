package klib.data.database.mdb.query

import klib.data.type.collections.biMapOf

internal class JavaQuery(
    private val query: com.healthmarketscience.jackcess.query.Query
) : Query {
    override val name: String
        get() = query.name

    override val type: Query.Type
        get() = TYPE_MAP[query.type]!!

    override val isHidden: Boolean
        get() = query.isHidden

    override val objectId: Int
        get() = query.objectId

    override val objectFlag: Int
        get() = query.objectFlag

    override val parameters: List<String>
        get() = query.parameters

    override val ownerAccessType: String
        get() = query.ownerAccessType

    override fun toSQLString(): String = query.toSQLString()

    public companion object {
        internal val TYPE_MAP = biMapOf(
            com.healthmarketscience.jackcess.query.Query.Type.SELECT to Query.Type.SELECT,
            com.healthmarketscience.jackcess.query.Query.Type.MAKE_TABLE to Query.Type.MAKE_TABLE,
            com.healthmarketscience.jackcess.query.Query.Type.APPEND to Query.Type.APPEND,
            com.healthmarketscience.jackcess.query.Query.Type.UPDATE to Query.Type.UPDATE,
            com.healthmarketscience.jackcess.query.Query.Type.DELETE to Query.Type.DELETE,
            com.healthmarketscience.jackcess.query.Query.Type.CROSS_TAB to Query.Type.CROSS_TAB,
            com.healthmarketscience.jackcess.query.Query.Type.DATA_DEFINITION to Query.Type.DATA_DEFINITION,
            com.healthmarketscience.jackcess.query.Query.Type.PASSTHROUGH to Query.Type.PASSTHROUGH,
            com.healthmarketscience.jackcess.query.Query.Type.UNION to Query.Type.UNION,
            com.healthmarketscience.jackcess.query.Query.Type.UNKNOWN to Query.Type.UNKNOWN,
        )
    }
}