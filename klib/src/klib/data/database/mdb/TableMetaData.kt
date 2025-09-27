package klib.data.database.mdb

import kotlinx.io.IOException

/**
 * Basic metadata about a single database Table.  This is the top-level
 * information stored in a (local) database which can be retrieved without
 * attempting to load the Table itself.
 *
 * @author Aziz Atoev
 * @usage _intermediate_class_
 */
public expect class TableMetaData {
    /**
     * The type of table
     */
    public val type: TableType

    /**
     * The name of the table (as it is stored in the database)
     */
    public val name: String

    /**
     * `true` if this is a linked table, `false` otherwise.
     */
    public val isLinked: Boolean

    /**
     * `true` if this is a system table, `false` otherwise.
     */
    public val isSystem: Boolean

    /**
     * The name of this linked table in the linked database if this is a linked
     * table, `null` otherwise.
     */
    public val linkedTableName: String

    /**
     * The name of this the linked database if this is a linked table, `null` otherwise.
     */
    public val linkedDbName: String

    /**
     * The connection of this the linked database if this is a linked ODBC
     * table, `null` otherwise.
     */
    public val connectionName: String

    /**
     * Opens this table from the given Database instance.
     */
    @Throws(IOException::class)
    public fun open(db: Database): Table

    /**
     * Gets the local table definition from the given Database instance if
     * available.  Only useful for linked ODBC tables.
     */
    @Throws(IOException::class)
    public fun getTableDefinition(db: Database): TableDefinition
}

public enum class TableType {
    LOCAL, LINKED, LINKED_ODBC
}
