package klib.data.db.mdb

/**
 * Information about a relationship between two tables in the database.
 *
 * @author Aziz Atoev
 */
public expect class Relationship(fromTable: String, toTable: String, database: Database) {

    public constructor(fromTable: Table, toTable: Table, database: Database)

    /** the name of this relationship  */
    public val name: String
    public val fromTable: Table
    public val fromColumns: List<Column>
    public val toTable: Table
    public val toColumns: List<Column>
    public val joinType: JoinType

    /** the various flags describing this relationship  */
    public val isOneToOne: Boolean
    public fun hasReferentialIntegrity(): Boolean
    public val cascadeUpdates: Boolean
    public val cascadeDeletes: Boolean
    public val cascadeNullOnDelete: Boolean
}

public enum class JoinType {
    INNER, LEFT_OUTER, RIGHT_OUTER
}
