package klib.data.db.mdb

import com.healthmarketscience.jackcess.RelationshipBuilder
import klib.data.type.collections.bimap.biMapOf

public actual class Relationship(public val relationship: com.healthmarketscience.jackcess.Relationship) {
    public actual constructor(fromTable: String, toTable: String, database: Database) :
            this(RelationshipBuilder(fromTable, toTable).toRelationship(database.database))

    public actual constructor(fromTable: Table, toTable: Table, database: Database) :
            this(RelationshipBuilder(fromTable.table, toTable.table).toRelationship(database.database))

    public actual val name: String
        get() = relationship.name

    public actual val fromTable: Table
        get() = Table(relationship.fromTable)

    public actual val fromColumns: List<Column>
        get() = relationship.fromColumns.map(::Column)

    public actual val toTable: Table
        get() = Table(relationship.toTable)

    public actual val toColumns: List<Column>
        get() = relationship.toColumns.map(::Column)

    public actual val joinType: JoinType
        get() = JOIN_TYPE_MAP[relationship.joinType]!!

    public actual val isOneToOne: Boolean
        get() = relationship.isOneToOne

    public actual fun hasReferentialIntegrity(): Boolean = relationship.hasReferentialIntegrity()

    public actual val cascadeUpdates: Boolean
        get() = relationship.cascadeUpdates()

    public actual val cascadeDeletes: Boolean
        get() = relationship.cascadeDeletes()

    public actual val cascadeNullOnDelete: Boolean
        get() = relationship.cascadeNullOnDelete()

    public companion object {
        internal val JOIN_TYPE_MAP = biMapOf(
            com.healthmarketscience.jackcess.Relationship.JoinType.INNER to JoinType.INNER,
            com.healthmarketscience.jackcess.Relationship.JoinType.LEFT_OUTER to JoinType.LEFT_OUTER,
            com.healthmarketscience.jackcess.Relationship.JoinType.RIGHT_OUTER to JoinType.RIGHT_OUTER,
        )
    }
}
