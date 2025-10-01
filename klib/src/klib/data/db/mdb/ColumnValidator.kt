package klib.data.db.mdb

import kotlinx.io.IOException

/**
 * Interface which allows for data manipulation/validation as values are being
 * inserted into a database.
 *
 * @author Aziz Atoev
 */
public fun interface ColumnValidator {
    /**
     * Validates and/or manipulates the given potential new value for the given
     * column.  This method may return an entirely different value or throw an
     * exception if the input value is not valid.
     */
    @Throws(IOException::class)
    public fun validate(column: Column, value: Any?): Any?
}
