package klib.data.database.mdb

/**
 * Factory which generates appropriate ColumnValidators when Column instances
 * are created.
 *
 * @author Aziz Atoev
 */
public fun interface ColumnValidatorFactory {
    /**
     * Returns a ColumnValidator instance for the given column, or `null`
     * if the default should be used.
     */
    public fun createValidator(column: Column): ColumnValidator?
}