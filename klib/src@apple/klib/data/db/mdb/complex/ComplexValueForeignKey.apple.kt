package klib.data.db.mdb.complex

import klib.data.db.mdb.Column
import kotlinx.datetime.LocalDateTime
import kotlinx.io.IOException

public actual class ComplexValueForeignKey public actual constructor(column: Column, value: Int) {
    public actual val column: Column
        get() = TODO("Not yet implemented")
    public actual val value: Int
        get() = TODO("Not yet implemented")
    public actual val complexType: ComplexDataType
        get() = TODO("Not yet implemented")

    @Throws(IOException::class)
    public actual fun countValues(): Int {
        TODO("Not yet implemented")
    }

    public actual val values: List<ComplexValue>
        get() = TODO("Not yet implemented")
    public actual val versions: List<Version>
        get() = TODO("Not yet implemented")
    public actual val attachments: List<Attachment>
        get() = TODO("Not yet implemented")
    public actual val multiValues: List<SingleValue>
        get() = TODO("Not yet implemented")
    public actual val unsupportedValues: List<UnsupportedValue>
        get() = TODO("Not yet implemented")

    public actual fun reset() {
    }

    @Throws(IOException::class)
    public actual fun addVersion(value: String): Version {
        TODO("Not yet implemented")
    }

    @Throws(IOException::class)
    public actual fun addVersion(
        value: String,
        modifiedDate: LocalDateTime
    ): Version {
        TODO("Not yet implemented")
    }

    @Throws(IOException::class)
    public actual fun addAttachment(data: ByteArray): Attachment {
        TODO("Not yet implemented")
    }

    @Throws(IOException::class)
    public actual fun addAttachment(
        url: String,
        name: String,
        type: String,
        data: ByteArray,
        timeStamp: LocalDateTime,
        flags: Int
    ): Attachment {
        TODO("Not yet implemented")
    }

    @Throws(IOException::class)
    public actual fun addEncodedAttachment(encodedData: ByteArray): Attachment {
        TODO("Not yet implemented")
    }

    @Throws(IOException::class)
    public actual fun addEncodedAttachment(
        url: String,
        name: String,
        type: String,
        encodedData: ByteArray,
        timeStamp: LocalDateTime,
        flags: Int
    ): Attachment {
        TODO("Not yet implemented")
    }

    @Throws(IOException::class)
    public actual fun updateAttachment(attachment: Attachment): Attachment {
        TODO("Not yet implemented")
    }

    @Throws(IOException::class)
    public actual fun deleteAttachment(attachment: Attachment): Attachment {
        TODO("Not yet implemented")
    }

    @Throws(IOException::class)
    public actual fun addMultiValue(value: Any?): SingleValue {
        TODO("Not yet implemented")
    }

    @Throws(IOException::class)
    public actual fun updateMultiValue(value: SingleValue): SingleValue {
        TODO("Not yet implemented")
    }

    @Throws(IOException::class)
    public actual fun deleteMultiValue(value: SingleValue): SingleValue {
        TODO("Not yet implemented")
    }

    @Throws(IOException::class)
    public actual fun addUnsupportedValue(values: MutableMap<String, *>): UnsupportedValue {
        TODO("Not yet implemented")
    }

    @Throws(IOException::class)
    public actual fun updateUnsupportedValue(value: UnsupportedValue): UnsupportedValue {
        TODO("Not yet implemented")
    }

    @Throws(IOException::class)
    public actual fun deleteUnsupportedValue(value: UnsupportedValue): UnsupportedValue {
        TODO("Not yet implemented")
    }

    @Throws(IOException::class)
    public actual fun deleteAllValues() {
    }

    public actual companion object {
        public actual val INVALID_ID: ComplexValue.Id
            get() = TODO("Not yet implemented")
        public actual val INVALID_FK: ComplexValueForeignKey
            get() = TODO("Not yet implemented")
    }
}
