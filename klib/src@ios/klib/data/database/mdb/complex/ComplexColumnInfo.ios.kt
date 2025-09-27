package klib.data.database.mdb.complex

import klib.data.database.mdb.Row
import okio.IOException

public actual  class ComplexColumnInfo<V : ComplexValue> {
    public actual  val type: ComplexDataType
        get() = TODO("Not yet implemented")

    @Throws(exceptionClasses = [IOException::class])
    public actual  fun countValues(complexValueFk: Int): Int {
        TODO("Not yet implemented")
    }

    @Throws(exceptionClasses = [IOException::class])
    public actual  fun getRawValues(complexValueFk: Int): List<Row> {
        TODO("Not yet implemented")
    }

    @Throws(exceptionClasses = [IOException::class])
    public actual  fun getRawValues(
        complexValueFk: Int,
        columnNames: Collection<String>
    ): List<Row> {
        TODO("Not yet implemented")
    }

    @Throws(exceptionClasses = [IOException::class])
    public actual  fun getValues(complexValueFk: ComplexValueForeignKey): List<V> {
        TODO("Not yet implemented")
    }

    @Throws(exceptionClasses = [IOException::class])
    public actual  fun addRawValue(rawValue: Map<String, *>): ComplexValue.Id {
        TODO("Not yet implemented")
    }

    @Throws(exceptionClasses = [IOException::class])
    public actual  fun addValue(value: V): ComplexValue.Id {
        TODO("Not yet implemented")
    }

    @Throws(exceptionClasses = [IOException::class])
    public actual  fun addValues(values: Collection<V>) {
    }

    @Throws(exceptionClasses = [IOException::class])
    public actual  fun updateRawValue(rawValue: Row): ComplexValue.Id {
        TODO("Not yet implemented")
    }

    @Throws(exceptionClasses = [IOException::class])
    public actual  fun updateValue(value: V): ComplexValue.Id {
        TODO("Not yet implemented")
    }

    @Throws(exceptionClasses = [IOException::class])
    public actual  fun updateValues(values: Collection<V>) {
    }

    @Throws(exceptionClasses = [IOException::class])
    public actual  fun deleteRawValue(rawValue: Row) {
    }

    @Throws(exceptionClasses = [IOException::class])
    public actual  fun deleteValue(value: V) {
    }

    @Throws(exceptionClasses = [IOException::class])
    public actual  fun deleteValues(values: Collection<V>) {
    }

    @Throws(exceptionClasses = [IOException::class])
    public actual  fun deleteAllValues(complexValueFk: Int) {
    }

    @Throws(exceptionClasses = [IOException::class])
    public actual  fun deleteAllValues(complexValueFk: ComplexValueForeignKey) {
    }
}