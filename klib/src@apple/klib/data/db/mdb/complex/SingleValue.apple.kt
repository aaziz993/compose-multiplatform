package klib.data.db.mdb.complex

import klib.data.db.mdb.Column

public actual class SingleValue public actual constructor(
    value: Any?,
    complexValueFk: ComplexValueForeignKey
) : ComplexValue {
    public actual var value: Any?
        get() = TODO("Not yet implemented")
        set(value) {}
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
}
