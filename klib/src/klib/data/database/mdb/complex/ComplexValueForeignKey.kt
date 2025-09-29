package klib.data.database.mdb.complex

import klib.data.database.mdb.Column
import kotlinx.datetime.LocalDateTime
import kotlinx.io.IOException


/**
 * Value which is returned for a complex column.  This value corresponds to a
 * foreign key in a secondary table which contains the actual complex data for
 * this row (which could be 0 or more complex values for a given row).  This
 * class contains various convenience methods for interacting with the actual
 * complex values.
 * <p>
 * This class will cache the associated complex values returned from one of
 * the lookup methods.  The various modification methods will clear this cache
 * automatically.  The {@link #reset} method may be called manually to clear
 * this internal cache.
 *
 * @author Aziz Atoev
 */

public expect class ComplexValueForeignKey(
    column: Column,
    value: Int
) {

    public val column: Column
    public val value: Int


    public val complexType: ComplexDataType

    @Throws(IOException::class)
    public fun countValues(): Int

    // @get:Throws(IOException::class)
    public val values: List<ComplexValue>

    // @get:Throws(IOException::class)
    public val versions: List<Version>

    // @get:Throws(IOException::class)
    public val attachments: List<Attachment>

    // @get:Throws(IOException::class)
    public val multiValues: List<SingleValue>

    // @get:Throws(IOException::class)
    public val unsupportedValues: List<UnsupportedValue>

    public fun reset()

    @Throws(IOException::class)
    public fun addVersion(value: String): Version

    @Throws(IOException::class)
    public fun addVersion(value: String, modifiedDate: LocalDateTime): Version

    @Throws(IOException::class)
    public fun addAttachment(data: ByteArray): Attachment

    @Throws(IOException::class)
    public fun addAttachment(
        url: String,
        name: String,
        type: String,
        data: ByteArray,
        timeStamp: LocalDateTime,
        flags: Int
    ): Attachment

    @Throws(IOException::class)
    public fun addEncodedAttachment(encodedData: ByteArray): Attachment

    @Throws(IOException::class)
    public fun addEncodedAttachment(
        url: String,
        name: String,
        type: String,
        encodedData: ByteArray,
        timeStamp: LocalDateTime,
        flags: Int
    ): Attachment

    @Throws(IOException::class)
    public fun updateAttachment(attachment: Attachment): Attachment

    @Throws(IOException::class)
    public fun deleteAttachment(attachment: Attachment): Attachment

    @Throws(IOException::class)
    public fun addMultiValue(value: Any?): SingleValue

    @Throws(IOException::class)
    public fun updateMultiValue(value: SingleValue): SingleValue

    @Throws(IOException::class)
    public fun deleteMultiValue(value: SingleValue): SingleValue

    @Throws(IOException::class)
    public fun addUnsupportedValue(values: MutableMap<String, *>): UnsupportedValue

    @Throws(IOException::class)
    public fun updateUnsupportedValue(value: UnsupportedValue): UnsupportedValue

    @Throws(IOException::class)
    public fun deleteUnsupportedValue(value: UnsupportedValue): UnsupportedValue

    @Throws(IOException::class)
    public fun deleteAllValues()

    public companion object {
        public val INVALID_ID: ComplexValue.Id

        public val INVALID_FK: ComplexValueForeignKey
    }
}
