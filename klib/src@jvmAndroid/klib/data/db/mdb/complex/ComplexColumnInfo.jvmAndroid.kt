package klib.data.db.mdb.complex

import klib.data.db.mdb.Row
import kotlinx.io.IOException

public actual class ComplexColumnInfo<V : ComplexValue>(
    private val complexColumnInfo: com.healthmarketscience.jackcess.complex.ComplexColumnInfo<com.healthmarketscience.jackcess.complex.ComplexValue>
) {
    public actual val type: ComplexDataType
        get() = COMPLEX_DATA_TYPES[complexColumnInfo.type]!!

    @Throws(IOException::class)
    public actual fun countValues(complexValueFk: Int): Int = complexColumnInfo.countValues(complexValueFk)

    @Throws(IOException::class)
    public actual fun getRawValues(complexValueFk: Int): List<Row> =
        complexColumnInfo.getRawValues(complexValueFk).map(::Row)

    @Throws(IOException::class)
    public actual fun getRawValues(complexValueFk: Int, columnNames: Collection<String>): List<Row> =
        complexColumnInfo.getRawValues(complexValueFk, columnNames).map(::Row)

    @Throws(IOException::class)
    @Suppress("UNCHECKED_CAST")
    public actual fun getValues(complexValueFk: ComplexValueForeignKey): List<V> =
        complexColumnInfo.getValues(complexValueFk.complexValueFk).map { value ->
            when (value) {
                is com.healthmarketscience.jackcess.complex.SingleValue -> SingleValue(value)
                is com.healthmarketscience.jackcess.complex.Attachment -> Attachment(value)
                is com.healthmarketscience.jackcess.complex.Version -> Version(value)
                is com.healthmarketscience.jackcess.complex.UnsupportedValue -> UnsupportedValue(value)

                else -> error("Unsupported value")
            } as V
        }

    @Throws(IOException::class)
    public actual fun addRawValue(rawValue: Map<String, *>): ComplexValue.Id =
        JavaComplexValue.JavaId(complexColumnInfo.addRawValue(rawValue))

    @Throws(IOException::class)
    public actual fun addValue(value: V): ComplexValue.Id =
        JavaComplexValue.JavaId(complexColumnInfo.addValue((value as JavaComplexValue).complexValue))

    @Throws(IOException::class)
    public actual fun addValues(values: Collection<V>): Unit =
        complexColumnInfo.addValues(values.map { value -> (value as JavaComplexValue).complexValue })

    @Throws(IOException::class)
    public actual fun updateRawValue(rawValue: Row): ComplexValue.Id =
        JavaComplexValue.JavaId(complexColumnInfo.updateRawValue(rawValue.row))

    @Throws(IOException::class)
    public actual fun updateValue(value: V): ComplexValue.Id =
        JavaComplexValue.JavaId(complexColumnInfo.updateValue((value as JavaComplexValue).complexValue))

    @Throws(IOException::class)
    public actual fun updateValues(values: Collection<V>): Unit =
        complexColumnInfo.updateValues(values.map { value -> (value as JavaComplexValue).complexValue })

    @Throws(IOException::class)
    public actual fun deleteRawValue(rawValue: Row): Unit =
        complexColumnInfo.deleteRawValue(rawValue.row)

    @Throws(IOException::class)
    public actual fun deleteValue(value: V): Unit =
        complexColumnInfo.deleteValue((value as JavaComplexValue).complexValue)

    @Throws(IOException::class)
    public actual fun deleteValues(values: Collection<V>): Unit =
        complexColumnInfo.deleteValues(values.map { value -> (value as JavaComplexValue).complexValue })

    @Throws(IOException::class)
    public actual fun deleteAllValues(complexValueFk: Int): Unit =
        complexColumnInfo.deleteAllValues(complexValueFk)

    @Throws(IOException::class)
    public actual fun deleteAllValues(complexValueFk: ComplexValueForeignKey): Unit =
        complexColumnInfo.deleteAllValues(complexValueFk.complexValueFk)
}
