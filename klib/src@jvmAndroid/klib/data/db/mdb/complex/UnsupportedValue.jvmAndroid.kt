package klib.data.db.mdb.complex

import com.healthmarketscience.jackcess.complex.UnsupportedValue
import com.healthmarketscience.jackcess.impl.complex.UnsupportedColumnInfoImpl

public actual class UnsupportedValue(public val unsupportedValue: UnsupportedValue) :
    ComplexValue by JavaComplexValue(unsupportedValue) {
    public actual constructor(
        values: Map<String, Any?>,
        complexValueFk: ComplexValueForeignKey
    ) : this(UnsupportedColumnInfoImpl.newValue(complexValueFk.complexValueFk, values))

    public actual val values: Map<String, Any?>
        get() = unsupportedValue.values

    public actual operator fun get(columnName: String): Any? = unsupportedValue.get(columnName)

    public actual operator fun set(columnName: String, value: Any?): Unit = unsupportedValue.set(columnName, value)
}
