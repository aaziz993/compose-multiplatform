package klib.data.database.mdb

import kotlinx.datetime.LocalDateTime
import kotlinx.io.IOException


/**
 * A single database table.  A Table instance is retrieved from a {@link
 * Database} instance.  The Table instance provides access to the table
 * metadata as well as the table data.  There are basic data operations on the
 * Table interface (i.e. {@link #iterator} {@link #addRow}, {@link #updateRow}
 * and {@link #deleteRow}), but for advanced search and data manipulation a
 * {@link Cursor} instance should be used.  New Tables can be created using a
 * {@link TableBuilder}.  The {@link com.healthmarketscience.jackcess.util.Joiner} utility can be used to traverse
 * table relationships (e.g. find rows in another table based on a foreign-key
 * relationship).
 * <p>
 * A Table instance is not thread-safe (see {@link Database} for more
 * thread-safety details).
 *
 * @author Aziz Atoev
 * @usage _general_class_
 */

public expect class Table(
    name: String,
    escapeIdentifiers: Boolean,
    database: Database,
) : Iterable<Row> {
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
     * Sets a new ErrorHandler.  If `null`, resets to using the
     * ErrorHandler configured at the Database level.
     * @usage _intermediate_method_
     */
    public var errorHandler: ErrorHandler?

    /**
     *  The currently configured auto number insert policy.
     * @see Database.isAllowAutoNumberInsert
     *
     * @usage _intermediate_method_
     */
    public var allowAutoNumberInsert: Boolean

    /**
     * @return All of the columns in this table (unmodifiable List)
     * @usage _general_method_
     */
    public val columns: List<Column>

    /**
     * @return the properties for this table
     * @usage _general_method_
     */
    // @get:Throws(IOException::class)
    public val properties: PropertyMap

    /**
     * @return the created date for this table if available
     * @usage _general_method_
     */
    public val createdDate: LocalDateTime

    /**
     * Note: jackcess *does not automatically update the modified date of a
     * Table*.
     *
     * @return the last updated date for this table if available
     * @usage _general_method_
     */

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

    /**
     * @usage _general_method_
     */
    public val rowCount: Int

    /**
     * Add multiple rows to this table, only writing to disk after all
     * rows have been written, and every time a data page is filled.  This
     * is much more efficient than calling [.addRow] multiple times.
     *
     *
     * Note, if this table has an auto-number column, the values written will be
     * put back into the given row arrays (assuming the given row array is at
     * least as long as the number of Columns in this Table).
     *
     *
     * Most exceptions thrown from this method will be wrapped with a [ ] which gives useful information in the case of a
     * partially successful write.
     *
     * @see .addRow
     * @param rows List of Object[] row values.  the rows will be modified if
     * this table contains an auto-number column, otherwise they
     * will not be modified.
     * @return the given row values list (unless row values were to small), with
     * appropriately sized row values (the ones passed in if long
     * enough).  the returned arrays will contain any autonumbers
     * generated
     * @usage _general_method_
     */
    public fun addRows(rows: List<Array<Any?>>): List<Array<Any?>>

    /**
     * Calls [.asRow] on the given row maps and passes the results to
     * [.addRows].
     *
     *
     * Note, if this table has an auto-number column, the values generated will
     * be put back into the appropriate row maps.
     *
     *
     * Most exceptions thrown from this method will be wrapped with a [ ] which gives useful information in the case of a
     * partially successful write.
     *
     * @return the given row map list, where the row maps will contain any
     * autonumbers generated
     * @usage _general_method_
     */
    @Throws(IOException::class)
    public fun addRowsFromMaps(rows: List<Map<String, Any?>>): List<Map<String, Any?>>


    /**
     * Update the given row.  Provided Row must have previously been returned
     * from this Table.
     * @return the given row, updated with the current row values
     * @throws IllegalStateException if the given row is not valid, or deleted.
     */
    public fun updateRow(row: Row): Row

    /**
     * Delete the given row.  Provided Row must have previously been returned
     * from this Table.
     * @return the given row
     * @throws IllegalStateException if the given row is not valid
     */
    public fun deleteRow(row: Row): Row

    /**
     * After calling this method, {@link #getNextRow} will return the first row
     * in the table, see {@link Cursor#reset} (uses the {@link #getDefaultCursor
     * default cursor}).
     * @usage _general_method_
     */
    public fun reset()

    override fun iterator(): Iterator<Row>
}

/**
 * enum which controls the ordering of the columns in a table.
 * @usage _intermediate_class_
 */
public enum class ColumnOrder {
    /** columns are ordered based on the order of the data in the table (this
     * order does not change as columns are added to the table).  */
    DATA,

    /** columns are ordered based on the "display" order (this order can be
     * changed arbitrarily)  */
    DISPLAY
}
