package klib.data.db.mdb


/**
 * Uniquely identifies a row of data within the access database.  While RowIds
 * are largely opaque identifiers, they are comparable to each other (within
 * the same table) and have valid {@code equals()}, {@code hashCode()} and
 * {@code toString()} methods.
 *
 * @author Aziz Atoev
 * @usage _intermediate_class_
 */
public expect class RowId(
    pageNumber: Int,
    rowNumber: Int
) : Comparable<RowId> {
    public val pageNumber: Int
    public val rowNumber: Int
    public val type: RowIdType

    override fun compareTo(other: RowId): Int

    public companion object {
        /** special page number which will sort before any other valid page
         * number  */
        public val FIRST_PAGE_NUMBER: Int

        /** special page number which will sort after any other valid page
         * number  */
        public val LAST_PAGE_NUMBER: Int

        /** special row number representing an invalid row number  */
        public val INVALID_ROW_NUMBER: Int

        public val FIRST_ROW_ID: RowId

        /** special rowId which will sort after any other valid rowId  */

        public val LAST_ROW_ID: RowId
    }
}

/** type attributes for RowIds which simplify comparisons  */
public enum class RowIdType {
    /** comparable type indicating this RowId should always compare less than
     * normal RowIds  */
    ALWAYS_FIRST,

    /** comparable type indicating this RowId should always compare
     * normally  */
    NORMAL,

    /** comparable type indicating this RowId should always compare greater
     * than normal RowIds  */
    ALWAYS_LAST
}
