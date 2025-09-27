package klib.data.database.mdb

import com.healthmarketscience.jackcess.Row
import com.healthmarketscience.jackcess.impl.RowIdImpl
import com.healthmarketscience.jackcess.impl.RowImpl
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import klib.data.database.mdb.complex.ComplexValueForeignKey
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import okio.IOException

public actual class Row(public val row: Row) : MutableMap<String, Any?> by row {
    public actual constructor(rowId: RowId) : this(RowImpl(rowId.rowId))

    public actual constructor(rowId: RowId, expectedSize: Int) : this(RowImpl(rowId.rowId, expectedSize))

    public actual constructor(row: klib.data.database.mdb.Row) : this(RowImpl(row.row))

    public actual val id: RowId
        get() = RowId(row.id as RowIdImpl)

    public actual fun getString(name: String): String = row.getString(name)

    public actual fun getBoolean(name: String): Boolean = row.getBoolean(name)

    public actual fun getByte(name: String): Byte = row.getByte(name)

    public actual fun getShort(name: String): Short = row.getShort(name)

    public actual fun getInt(name: String): Int = row.getInt(name)

    public actual fun getBigDecimal(name: String): BigDecimal =
        BigDecimal.parseString(row.getBigDecimal(name).toPlainString())

    public actual fun getFloat(name: String): Float = row.getFloat(name)

    public actual fun getDouble(name: String): Double = row.getDouble(name)

    public actual fun getLocalDateTime(name: String): LocalDateTime = row.getLocalDateTime(name).toKotlinLocalDateTime()

    public actual fun getBytes(name: String): ByteArray = row.getBytes(name)

    public actual fun getForeignKey(name: String): ComplexValueForeignKey =
        ComplexValueForeignKey(row.getForeignKey(name))

    @Throws(IOException::class)
    public actual fun getBlob(name: String): OleBlob = OleBlob(row.getBlob(name))
}
