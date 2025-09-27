package klib.data.database.mdb.complex

import klib.data.database.mdb.Column
import klib.data.database.mdb.RowId
import kotlinx.io.IOException


/**
 * Base interface for a value in a complex column (where there may be multiple
 * values for a single row in the main table).
 *
 * @author Aziz Atoev
 */

public interface ComplexValue {
    /**
     * Called once when a new ComplexValue is saved to set the new unique
     * identifier.
     */
    public var id: Id

    /**
     * Returns the foreign key identifier for this complex value (this value is
     * the same for all values in the same row of the main table).
     *
     * @return the current id or [ComplexColumnInfoImpl.INVALID_FK]
     * for a new, unsaved value.
     */
    public var complexValueForeignKey: ComplexValueForeignKey


    /**
     * @return the column in the main table with which this complex value is
     * associated
     */
    public val column: Column

    /**
     * Writes any updated data for this complex value to the database.
     */
    @Throws(IOException::class)
    public fun update()

    /**
     * Deletes the data for this complex value from the database.
     */
    @Throws(IOException::class)
    public fun delete()


    /**
     * Identifier for a ComplexValue.  Only valid for comparing complex values
     * for the same column.
     */
    public abstract class Id {

        /**
         * Returns the unique identifier of this complex value (this value is unique
         * among all values in all rows of the main table for the complex column).
         */
        public abstract fun get(): Int

        /**
         * Returns the rowId of this ComplexValue within the secondary table.
         */
        public abstract val rowId: RowId
    }
}
