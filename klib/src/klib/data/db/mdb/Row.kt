package klib.data.db.mdb

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import klib.data.db.mdb.complex.ComplexValueForeignKey
import kotlinx.datetime.LocalDateTime
import kotlinx.io.IOException

/**
 * A row of data as column name-&gt;value pairs.  Values are strongly typed, and
 * column names are case sensitive.
 *
 * @author Aziz Atoev
 * @usage _general_class_
 */
public expect class Row(rowId: RowId) : MutableMap<String, Any?> {

    public constructor(rowId: RowId, expectedSize: Int)

    public constructor(row: Row)

    /**
     * @return the id of this row
     */
    public val id: RowId

    /**
     * Convenience method which gets the value for the row with the given name,
     * casting it to a String (DataTypes TEXT, MEMO, GUID).
     */
    public fun getString(name: String): String

    /**
     * Convenience method which gets the value for the row with the given name,
     * casting it to a Boolean (DataType BOOLEAN).
     */
    public fun getBoolean(name: String): Boolean

    /**
     * Convenience method which gets the value for the row with the given name,
     * casting it to a Byte (DataType BYTE).
     */
    public fun getByte(name: String): Byte

    /**
     * Convenience method which gets the value for the row with the given name,
     * casting it to a Short (DataType INT).
     */
    public fun getShort(name: String): Short

    /**
     * Convenience method which gets the value for the row with the given name,
     * casting it to a Integer (DataType LONG).
     */
    public fun getInt(name: String): Int

    /**
     * Convenience method which gets the value for the row with the given name,
     * casting it to a BigDecimal (DataTypes MONEY, NUMERIC).
     */
    public fun getBigDecimal(name: String): BigDecimal

    /**
     * Convenience method which gets the value for the row with the given name,
     * casting it to a Float (DataType FLOAT).
     */
    public fun getFloat(name: String): Float

    /**
     * Convenience method which gets the value for the row with the given name,
     * casting it to a Double (DataType DOUBLE).
     */
    public fun getDouble(name: String): Double

    /**
     * Convenience method which gets the value for the row with the given name,
     * casting it to a LocalDateTime (DataType SHORT_DATE_TIME or
     * EXT_DATE_TIME).  This method will only work for Database instances
     * configured for [DateTimeType.LOCAL_DATE_TIME].
     */
    public fun getLocalDateTime(name: String): LocalDateTime

    /**
     * Convenience method which gets the value for the row with the given name,
     * casting it to a byte[] (DataTypes BINARY, OLE).
     */
    public fun getBytes(name: String): ByteArray

    /**
     * Convenience method which gets the value for the row with the given name,
     * casting it to a [klib.data.db.mdb.complex.ComplexValueForeignKey] (DataType COMPLEX_TYPE).
     */
    public fun getForeignKey(name: String): ComplexValueForeignKey

    /**
     * Convenience method which gets the value for the row with the given name,
     * converting it to an [OleBlob] (DataTypes OLE).
     *
     *
     * Note, *the OleBlob should be closed after use*.
     */
    @Throws(IOException::class)
    public fun getBlob(name: String): OleBlob
    override val keys: MutableSet<String>
    override val values: MutableCollection<Any?>
    override val entries: MutableSet<MutableMap.MutableEntry<String, Any?>>
    override fun put(key: String, value: Any?): Any?
    override fun remove(key: String): Any?
    override fun putAll(from: Map<out String, Any?>)
    override fun clear()
    override val size: Int
    override fun isEmpty(): Boolean
    override fun containsKey(key: String): Boolean
    override fun containsValue(value: Any?): Boolean
    override fun get(key: String): Any?
}
