package klib.data.database.mdb.complex

import com.healthmarketscience.jackcess.complex.SingleValue
import com.healthmarketscience.jackcess.impl.complex.MultiValueColumnInfoImpl

public actual class SingleValue(public val singleValue: SingleValue) :
    ComplexValue by JavaComplexValue(singleValue) {
    public actual constructor(value: Any?, complexValueFk: ComplexValueForeignKey) :
            this(MultiValueColumnInfoImpl.newSingleValue(complexValueFk.complexValueFk, value))

    public actual var value: Any?
        get() = singleValue.get()
        set(value) {
            singleValue.set(value)
        }
}