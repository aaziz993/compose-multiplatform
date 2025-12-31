package klib.data.database.mdb

public actual class Relationship() {
    public actual constructor(
        fromTable: String,
        toTable: String,
        database: Database
    ) : this()

    public actual constructor(
        fromTable: Table,
        toTable: Table,
        database: Database
    ) : this()

    public actual val name: String
        get() = TODO("Not yet implemented")
    public actual val fromTable: Table
        get() = TODO("Not yet implemented")
    public actual val fromColumns: List<Column>
        get() = TODO("Not yet implemented")
    public actual val toTable: Table
        get() = TODO("Not yet implemented")
    public actual val toColumns: List<Column>
        get() = TODO("Not yet implemented")
    public actual val joinType: JoinType
        get() = TODO("Not yet implemented")
    public actual val isOneToOne: Boolean
        get() = TODO("Not yet implemented")

    public actual fun hasReferentialIntegrity(): Boolean {
        TODO("Not yet implemented")
    }

    public actual val cascadeUpdates: Boolean
        get() = TODO("Not yet implemented")
    public actual val cascadeDeletes: Boolean
        get() = TODO("Not yet implemented")
    public actual val cascadeNullOnDelete: Boolean
        get() = TODO("Not yet implemented")
}
