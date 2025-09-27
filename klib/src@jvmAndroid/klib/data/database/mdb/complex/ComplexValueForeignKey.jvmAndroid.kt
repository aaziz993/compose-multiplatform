package klib.data.database.mdb.complex

import com.healthmarketscience.jackcess.impl.complex.ComplexColumnInfoImpl
import com.healthmarketscience.jackcess.impl.complex.ComplexValueForeignKeyImpl
import klib.data.database.mdb.Column
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import okio.IOException

public actual class ComplexValueForeignKey(
    public val complexValueFk: com.healthmarketscience.jackcess.complex.ComplexValueForeignKey
) {

    public actual constructor(column: Column, value: Int) :
            this(ComplexValueForeignKeyImpl(column.column, value))

    public actual val column: Column
        get() = Column(complexValueFk.column)

    public actual val value: Int
        get() = complexValueFk.get()

    public actual val complexType: ComplexDataType = COMPLEX_DATA_TYPE_MAP[complexValueFk.complexType]!!

    @Throws(IOException::class)
    public actual fun countValues(): Int = complexValueFk.countValues()

    public actual val values: List<ComplexValue>
        @Throws(IOException::class)
        get() = complexValueFk.values.map(::JavaComplexValue)

    public actual val versions: List<Version>
        @Throws(IOException::class)
        get() = complexValueFk.versions.map(::Version)

    public actual val attachments: List<Attachment>
        @Throws(IOException::class)
        get() = complexValueFk.attachments.map(::Attachment)

    public actual val multiValues: List<SingleValue>
        @Throws(IOException::class)
        get() = complexValueFk.multiValues.map(::SingleValue)


    public actual val unsupportedValues: List<UnsupportedValue>
        @Throws(IOException::class)
        get() = complexValueFk.unsupportedValues.map(::UnsupportedValue)

    public actual fun reset(): Unit = complexValueFk.reset()

    @Throws(IOException::class)
    public actual fun addVersion(value: String): Version = Version(complexValueFk.addVersion(value))

    @Throws(IOException::class)
    public actual fun addVersion(value: String, modifiedDate: LocalDateTime): Version =
        Version(complexValueFk.addVersion(value, modifiedDate.toJavaLocalDateTime()))

    @Throws(IOException::class)
    public actual fun addAttachment(data: ByteArray): Attachment =
        Attachment(complexValueFk.addAttachment(data))

    @Throws(IOException::class)
    public actual fun addAttachment(
        url: String,
        name: String,
        type: String,
        data: ByteArray,
        timeStamp: LocalDateTime,
        flags: Int
    ): Attachment = Attachment(
        complexValueFk.addAttachment(
            url,
            name,
            type,
            data,
            timeStamp.toJavaLocalDateTime(),
            flags
        )
    )

    @Throws(IOException::class)
    public actual fun addEncodedAttachment(encodedData: ByteArray): Attachment =
        Attachment(complexValueFk.addEncodedAttachment(encodedData))

    @Throws(IOException::class)
    public actual fun addEncodedAttachment(
        url: String,
        name: String,
        type: String,
        encodedData: ByteArray,
        timeStamp: LocalDateTime,
        flags: Int
    ): Attachment = Attachment(
        complexValueFk.addEncodedAttachment(
            url,
            name,
            type,
            encodedData,
            timeStamp.toJavaLocalDateTime(),
            flags
        )
    )

    @Throws(IOException::class)
    public actual fun updateAttachment(attachment: Attachment): Attachment =
        Attachment(complexValueFk.updateAttachment(attachment.attachment))

    @Throws(IOException::class)
    public actual fun deleteAttachment(attachment: Attachment): Attachment =
        Attachment(complexValueFk.deleteAttachment(attachment.attachment))

    @Throws(IOException::class)
    public actual fun addMultiValue(value: Any?): SingleValue =
        SingleValue(complexValueFk.addMultiValue(value))

    @Throws(IOException::class)
    public actual fun updateMultiValue(value: SingleValue): SingleValue =
        SingleValue(complexValueFk.updateMultiValue(value.singleValue))

    @Throws(IOException::class)
    public actual fun deleteMultiValue(value: SingleValue): SingleValue =
        SingleValue(complexValueFk.deleteMultiValue(value.singleValue))


    @Throws(IOException::class)
    public actual fun addUnsupportedValue(values: MutableMap<String, *>): UnsupportedValue =
        UnsupportedValue(complexValueFk.addUnsupportedValue(values))

    @Throws(IOException::class)
    public actual fun updateUnsupportedValue(value: UnsupportedValue): UnsupportedValue =
        UnsupportedValue(
            complexValueFk.updateUnsupportedValue(value.unsupportedValue)
        )

    @Throws(IOException::class)
    public actual fun deleteUnsupportedValue(value: UnsupportedValue): UnsupportedValue =
        UnsupportedValue(
            complexValueFk.deleteUnsupportedValue(value.unsupportedValue)
        )

    @Throws(IOException::class)
    public actual fun deleteAllValues(): Unit = complexValueFk.deleteAllValues()

    public actual companion object {
        public actual val INVALID_ID: ComplexValue.Id = JavaComplexValue.JavaId(ComplexColumnInfoImpl.INVALID_ID)
        public actual val INVALID_FK: ComplexValueForeignKey =
            ComplexValueForeignKey(ComplexColumnInfoImpl.INVALID_FK)
    }
}