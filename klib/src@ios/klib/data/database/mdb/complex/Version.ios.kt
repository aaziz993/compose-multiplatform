package klib.data.database.mdb.complex

import klib.data.database.mdb.Column
import kotlinx.datetime.LocalDateTime

public actual class Version public actual constructor(
    value: String,
    modifiedDate: Any,
    complexValueFk: ComplexValueForeignKey
) : ComplexValue, Comparable<Version> {
    public actual val value: String
        get() = TODO("Not yet implemented")
    public actual val modifiedLocalDate: LocalDateTime
        get() = TODO("Not yet implemented")
    public actual val modifiedDateObject: Any
        get() = TODO("Not yet implemented")
    public actual override var id: ComplexValue.Id
        get() = TODO("Not yet implemented")
        set(value) {}
    public actual override var complexValueForeignKey: ComplexValueForeignKey
        get() = TODO("Not yet implemented")
        set(value) {}
    public actual override val column: Column
        get() = TODO("Not yet implemented")

    public actual override fun update() {
    }

    public actual override fun delete() {
    }

    public actual override operator fun compareTo(other: Version): Int {
        TODO("Not yet implemented")
    }
}