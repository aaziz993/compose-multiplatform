package klib.data.db.mdb.complex

import klib.data.db.mdb.Column


/**
 * Complex value corresponding to an single value in a multi-value column.
 *
 * @author Aziz Atoev
 */

public expect class SingleValue(
    value: Any?,
    complexValueFk: ComplexValueForeignKey = ComplexValueForeignKey.INVALID_FK,
) : ComplexValue {
    public var value: Any?

    override var id: ComplexValue.Id
    override var complexValueForeignKey: ComplexValueForeignKey
    override val column: Column
    override fun update()
    override fun delete()
}
