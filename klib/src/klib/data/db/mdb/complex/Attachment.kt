package klib.data.db.mdb.complex

import klib.data.db.mdb.Column
import kotlinx.datetime.LocalDateTime


/**
 * Complex value corresponding to an attachment.
 *
 * @author Aziz Atoev
 */

public expect class Attachment(
    fileData: ByteArray,
    encodedFileData: ByteArray,
    fileName: String,
    fileUrl: String,
    fileType: String,
    fileLocalTimeStamp: LocalDateTime,
    fileTimeStampObject: Any,
    fileFlags: Int,
    complexValueFk: ComplexValueForeignKey = ComplexValueForeignKey.INVALID_FK,
) : ComplexValue {

    public var fileData: ByteArray

    public var encodedFileData: ByteArray

    public var fileName: String

    public var fileUrl: String

    public var fileType: String

    public var fileLocalTimeStamp: LocalDateTime

    public val fileTimeStampObject: Any

    public var fileFlags: Int

    override var id: ComplexValue.Id
    override var complexValueForeignKey: ComplexValueForeignKey
    override val column: Column
    override fun update()
    override fun delete()
}
