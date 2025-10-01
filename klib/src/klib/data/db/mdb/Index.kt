package klib.data.db.mdb

// import kotlinx.io.IOException

/**
 * Access database index definition.  A [Table] has a list of Index
 * instances.  Indexes can enable fast searches and ordered traversal on a
 * Table (for the indexed columns).  These features can be utilized via an
 * [IndexCursor].
 *
 * @author Aziz Atoev
 * @usage _general_class_
 */
public expect class Index
    (
    name: String,
    table: Table,
    columnNames: List<String> = emptyList(),
    descendingColumnNames: List<String> = emptyList(),
) {
    public constructor(
        name: String,
        tableDefinition: TableDefinition,
        columnNames: List<String> = emptyList(),
        descendingColumnNames: List<String> = emptyList(),
    )

    public val table: Table

    public val name: String

    public val isPrimaryKey: Boolean

    public val isForeignKey: Boolean

    /**
     * @return the Columns for this index (unmodifiable)
     */
    public val columns: List<Column>

    /**
     * @return the Index referenced by this Index's ForeignKeyReference (if it
     * has one), otherwise `null`.
     */
    // @get:Throws(IOException::class)
    public val referencedIndex: Index

    /**
     * Whether or not `null` values are actually recorded in the index.
     */
    public val shouldIgnoreNulls: Boolean

    /**
     * Whether or not index entries must be unique.
     *
     *
     * Some notes about uniqueness:
     *
     *  * Access does not seem to consider multiple `null` entries
     * invalid for a unique index
     *  * text indexes collapse case, and Access seems to compare **only**
     * the index entry bytes, therefore two strings which differ only in
     * case *will violate* the unique constraint
     *
     */
    public val isUnique: Boolean

    /**
     * Whether or not values are required for index columns.
     */
    public val isRequired: Boolean

    /**
     * Information about a Column in an Index
     */
    public class Column {
        public val column: klib.data.db.mdb.Column

        public val isAscending: Boolean

        public val columnIndex: Int

        public val name: String
    }
}
