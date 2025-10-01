package klib.data.db.mdb.complex

import klib.data.db.mdb.Column

public actual class UnsupportedValue public actual constructor(
    values: Map<String, Any?>,
    complexValueFk: ComplexValueForeignKey
) : ComplexValue {
    public actual val values: Map<String, Any?>
        get() = TODO("Not yet implemented")

    public actual operator fun get(columnName: String): Any? {
        TODO("Not yet implemented")
    }

    public actual operator fun set(columnName: String, value: Any?) {
    }

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
