package klib.data.database.mdb

import kotlinx.io.IOException

/**
 * Handler for errors encountered while reading a column of row data from a
 * Table.  An instance of this class may be configured at the Database, Table,
 * or Cursor level to customize error handling as desired.  The default
 * instance used is [.DEFAULT], which just rethrows any exceptions
 * encountered.
 *
 * @author Aziz Atoev
 * @usage _intermediate_class_
 */
public fun interface ErrorHandler {
    /**
     * Handles an error encountered while reading a column of data from a Table
     * row.  Handler may either throw an exception (which will be propagated
     * back to the caller) or return a replacement for this row's column value
     * (in which case the row will continue to be read normally).
     *
     * @param column the info for the column being read
     * @param columnData the actual column data for the column being read (which
     * may be `null` depending on when the exception
     * was thrown during the reading process)
     * @param location the current location of the error
     * @param error the error that was encountered
     *
     * @return replacement for this row's column
     */
    @Throws(IOException::class)
    public fun handleRowError(
        column: Column,
        columnData: ByteArray,
        location: Location,
        error: Exception
    ): Any

    /**
     * Provides location information for an error.
     */
    public interface Location {
        /**
         * @return the table in which the error occurred
         */
        public val table: Table

        /**
         * Contains details about the errored row, useful for debugging.
         */
        override fun toString(): String
    }

    public companion object {
        /**
         * default error handler used if none provided (just rethrows exception)
         * @usage _general_field_
         */
        public val DEFAULT: ErrorHandler = object : ErrorHandler {
            @Throws(IOException::class)
            override fun handleRowError(
                column: Column,
                columnData: ByteArray,
                location: Location,
                error: Exception
            ): Any {
                // really can only be RuntimeException or IOException
                if (error is IOException) throw error

                throw error as RuntimeException
            }
        }
    }
}
