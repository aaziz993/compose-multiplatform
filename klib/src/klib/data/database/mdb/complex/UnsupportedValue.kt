package klib.data.database.mdb.complex

import klib.data.database.mdb.Column


/**
 * Base interface for a value in a complex column (where there may be multiple
 * values for a single row in the main table).
 *
 * @author Aziz Atoev
 */
public expect class UnsupportedValue(
    values: Map<String, Any?>,
    complexValueFk: ComplexValueForeignKey = ComplexValueForeignKey.INVALID_FK
) : ComplexValue {
    public val values: Map<String, Any?>

    public operator fun get(columnName: String): Any?

    public operator fun set(columnName: String, value: Any?)
    override var id: ComplexValue.Id
    override var complexValueForeignKey: ComplexValueForeignKey
    override val column: Column
    override fun update()
    override fun delete()
}
