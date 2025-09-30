package klib.data.database.mdb

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import klib.data.database.mdb.complex.ComplexValueForeignKey
import kotlinx.datetime.LocalDateTime
import kotlinx.io.IOException

public actual class Row() : MutableMap<String, Any?> {

    public actual constructor(rowId: RowId) : this()

    public actual constructor(rowId: RowId, expectedSize: Int) : this()

    public actual constructor(row: Row) : this()

    public actual val id: RowId
        get() = TODO("Not yet implemented")

    public actual fun getString(name: String): String {
        TODO("Not yet implemented")
    }

    public actual fun getBoolean(name: String): Boolean {
        TODO("Not yet implemented")
    }

    public actual fun getByte(name: String): Byte {
        TODO("Not yet implemented")
    }

    public actual fun getShort(name: String): Short {
        TODO("Not yet implemented")
    }

    public actual fun getInt(name: String): Int {
        TODO("Not yet implemented")
    }

    public actual fun getBigDecimal(name: String): BigDecimal {
        TODO("Not yet implemented")
    }

    public actual fun getFloat(name: String): Float {
        TODO("Not yet implemented")
    }

    public actual fun getDouble(name: String): Double {
        TODO("Not yet implemented")
    }

    public actual fun getLocalDateTime(name: String): LocalDateTime {
        TODO("Not yet implemented")
    }

    public actual fun getBytes(name: String): ByteArray {
        TODO("Not yet implemented")
    }

    public actual fun getForeignKey(name: String): ComplexValueForeignKey {
        TODO("Not yet implemented")
    }

    @Throws(IOException::class)
    public actual fun getBlob(name: String): OleBlob {
        TODO("Not yet implemented")
    }

    public actual override val keys: MutableSet<String>
        get() = TODO("Not yet implemented")
    public actual override val values: MutableCollection<Any?>
        get() = TODO("Not yet implemented")
    public actual override val entries: MutableSet<MutableMap.MutableEntry<String, Any?>>
        get() = TODO("Not yet implemented")

    public actual override fun put(key: String, value: Any?): Any? {
        TODO("Not yet implemented")
    }

    public actual override fun remove(key: String): Any? {
        TODO("Not yet implemented")
    }

    public actual override fun putAll(from: Map<out String, Any?>) {
    }

    public actual override fun clear() {
    }

    public actual override val size: Int
        get() = TODO("Not yet implemented")

    public actual override fun isEmpty(): Boolean {
        TODO("Not yet implemented")
    }

    public actual override fun containsKey(key: String): Boolean {
        TODO("Not yet implemented")
    }

    public actual override fun containsValue(value: Any?): Boolean {
        TODO("Not yet implemented")
    }

    public actual override operator fun get(key: String): Any? {
        TODO("Not yet implemented")
    }
}
