package klib.data.db.mdb.complex

import com.healthmarketscience.jackcess.impl.RowIdImpl
import klib.data.db.mdb.Column
import klib.data.db.mdb.RowId

internal class JavaComplexValue(
    public val complexValue: com.healthmarketscience.jackcess.complex.ComplexValue,
) : ComplexValue {

    override var id: ComplexValue.Id
        get() = JavaId(complexValue.id)
        set(value) {
            complexValue.id = (value as JavaId).id
        }
    override var complexValueForeignKey: ComplexValueForeignKey
        get() = ComplexValueForeignKey(complexValue.complexValueForeignKey)
        set(value) {
            complexValue.complexValueForeignKey = value.complexValueFk

        }

    override val column: Column
        get() = Column(complexValue.column)

    override fun update(): Unit = complexValue.update()

    override fun delete(): Unit = complexValue.delete()

    public class JavaId(public val id: com.healthmarketscience.jackcess.complex.ComplexValue.Id) : ComplexValue.Id() {
        override fun get(): Int = id.get()

        override val rowId: RowId
            get() = RowId(id.rowId as RowIdImpl)
    }
}
