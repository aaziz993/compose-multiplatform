package klib.data.db.mdb.complex

import klib.data.db.mdb.Column
import kotlinx.datetime.LocalDateTime


/**
 * Complex value corresponding to a version of a memo column.
 *
 * @author Aziz Atoev
 */
public expect class Version(
    value: String,
    modifiedDate: Any,
    complexValueFk: ComplexValueForeignKey = ComplexValueForeignKey.INVALID_FK
) : ComplexValue, Comparable<Version> {
    public val value: String

    public val modifiedLocalDate: LocalDateTime

    public val modifiedDateObject: Any

    override var id: ComplexValue.Id
    override var complexValueForeignKey: ComplexValueForeignKey
    override val column: Column
    override fun update()
    override fun delete()
    override fun compareTo(other: Version): Int
}
