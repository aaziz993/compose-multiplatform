package klib.data.database.mdb.complex

import com.healthmarketscience.jackcess.complex.Attachment
import com.healthmarketscience.jackcess.impl.complex.AttachmentColumnInfoImpl
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime

public actual class Attachment(public val attachment: Attachment) :
    ComplexValue by JavaComplexValue(attachment) {
    public actual constructor(
        fileData: ByteArray,
        encodedFileData: ByteArray,
        fileName: String,
        fileUrl: String,
        fileType: String,
        fileLocalTimeStamp: LocalDateTime,
        fileTimeStampObject: Any,
        fileFlags: Int,
        complexValueFk: ComplexValueForeignKey
    ) : this(
        AttachmentColumnInfoImpl.newAttachment(
            complexValueFk.complexValueFk,
            fileUrl, fileName, fileType, fileData, fileLocalTimeStamp, 0
        )
    )

    public actual var fileData: ByteArray
        get() = attachment.fileData
        set(value) {
            attachment.fileData = value
        }

    public actual var encodedFileData: ByteArray
        get() = attachment.encodedFileData
        set(value) {
            attachment.encodedFileData = value
        }

    public actual var fileName: String
        get() = attachment.fileName
        set(value) {
            attachment.fileName = value
        }

    public actual var fileUrl: String
        get() = attachment.fileUrl
        set(value) {
            attachment.fileUrl = value
        }

    public actual var fileType: String
        get() = attachment.fileType
        set(value) {
            attachment.fileType = value
        }

    public actual var fileLocalTimeStamp: LocalDateTime
        get() = attachment.fileLocalTimeStamp.toKotlinLocalDateTime()
        set(value) {
            attachment.fileLocalTimeStamp = value.toJavaLocalDateTime()
        }

    public actual val fileTimeStampObject: Any
        get() = attachment.fileTimeStampObject

    public actual var fileFlags: Int
        get() = attachment.fileFlags
        set(value) {
            attachment.fileFlags = value
        }
}