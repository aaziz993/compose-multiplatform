package klib.data.database.mdb

import kotlinx.datetime.LocalDateTime
import kotlinx.io.IOException

/**
 * The definition of a single database table.  A TableDefinition instance is
 * retrieved from a [TableMetaData] instance.  The TableDefinition
 * instance only provides access to the table metadata, but no table data.
 *
 *
 * A TableDefinition instance is not thread-safe (see [Database] for
 * more thread-safety details).
 *
 * @author Aziz Atoev
 * @usage _intermediate_class_
 */
public expect class TableDefinition {
    /**
     * @return The name of the table
     * @usage _general_method_
     */
    public val name: String

    /**
     * Whether or not this table has been marked as hidden.
     * @usage _general_method_
     */
    public val isHidden: Boolean

    /**
     * Whether or not this table is a system (internal) table.
     * @usage _general_method_
     */
    public val isSystem: Boolean

    /**
     * @usage _general_method_
     */
    public val database: Database

    /**
     * @return All of the columns in this table (unmodifiable List)
     * @usage _general_method_
     */
    public val columns: List<Column>

    /**
     * @return the column with the given name
     * @usage _general_method_
     */
    public fun getColumn(name: String): Column

    /**
     * @return the properties for this table
     * @usage _general_method_
     */
    @get:Throws(IOException::class)
    public val properties: PropertyMap

    /**
     * @return the created date for this table if available
     * @usage _general_method_
     */
    @get:Throws(IOException::class)
    public val createdDate: LocalDateTime

    /**
     * Note: jackcess *does not automatically update the modified date of a
     * Table*.
     *
     * @return the last updated date for this table if available
     * @usage _general_method_
     */
    @get:Throws(IOException::class)
    public val updatedDate: LocalDateTime

    /**
     * @return All of the Indexes on this table (unmodifiable List)
     * @usage _intermediate_method_
     */
    public val indexes: List<Index>

    /**
     * @return the index with the given name
     * @throws IllegalArgumentException if there is no index with the given name
     * @usage _intermediate_method_
     */
    public fun getIndex(name: String): Index

    /**
     * @return the primary key index for this table
     * @throws IllegalArgumentException if there is no primary key index on this
     * table
     * @usage _intermediate_method_
     */
    public val primaryKeyIndex: Index

    /**
     * @return the foreign key index joining this table to the given other table
     * @throws IllegalArgumentException if there is no relationship between this
     * table and the given table
     * @usage _intermediate_method_
     */
    public fun getForeignKeyIndex(otherTable: Table): Index
}
