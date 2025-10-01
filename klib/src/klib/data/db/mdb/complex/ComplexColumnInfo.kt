package klib.data.db.mdb.complex

import klib.data.db.mdb.Row
import kotlinx.io.IOException

/**
 * Base class for the additional information tracked for complex columns.
 *
 * @author Aziz Atoev
 */
public expect class ComplexColumnInfo<V : ComplexValue> {
    public val type: ComplexDataType

    @Throws(IOException::class)
    public fun countValues(complexValueFk: Int): Int

    @Throws(IOException::class)
    public fun getRawValues(complexValueFk: Int): List<Row>

    @Throws(IOException::class)
    public fun getRawValues(complexValueFk: Int, columnNames: Collection<String>): List<Row>

    @Throws(IOException::class)
    public fun getValues(complexValueFk: ComplexValueForeignKey): List<V>

    @Throws(IOException::class)
    public fun addRawValue(rawValue: Map<String, *>): ComplexValue.Id

    @Throws(IOException::class)
    public fun addValue(value: V): ComplexValue.Id

    @Throws(IOException::class)
    public fun addValues(values: Collection<V>)

    @Throws(IOException::class)
    public fun updateRawValue(rawValue: Row): ComplexValue.Id

    @Throws(IOException::class)
    public fun updateValue(value: V): ComplexValue.Id

    @Throws(IOException::class)
    public fun updateValues(values: Collection<V>)

    @Throws(IOException::class)
    public fun deleteRawValue(rawValue: Row)

    @Throws(IOException::class)
    public fun deleteValue(value: V)

    @Throws(IOException::class)
    public fun deleteValues(values: Collection<V>)

    @Throws(IOException::class)
    public fun deleteAllValues(complexValueFk: Int)

    @Throws(IOException::class)
    public fun deleteAllValues(complexValueFk: ComplexValueForeignKey)
}
