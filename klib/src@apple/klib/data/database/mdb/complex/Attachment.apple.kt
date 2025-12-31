package klib.data.database.mdb.complex

import klib.data.database.mdb.Column
import kotlinx.datetime.LocalDateTime

public actual  class Attachment public actual  constructor(
    fileData: ByteArray,
    encodedFileData: ByteArray,
    fileName: String,
    fileUrl: String,
    fileType: String,
    fileLocalTimeStamp: LocalDateTime,
    fileTimeStampObject: Any,
    fileFlags: Int,
    complexValueFk: ComplexValueForeignKey
) : ComplexValue {
    public actual  var fileData: ByteArray
        get() = TODO("Not yet implemented")
        set(value) {}
    public actual  var encodedFileData: ByteArray
        get() = TODO("Not yet implemented")
        set(value) {}
    public actual  var fileName: String
        get() = TODO("Not yet implemented")
        set(value) {}
    public actual  var fileUrl: String
        get() = TODO("Not yet implemented")
        set(value) {}
    public actual  var fileType: String
        get() = TODO("Not yet implemented")
        set(value) {}
    public actual  var fileLocalTimeStamp: LocalDateTime
        get() = TODO("Not yet implemented")
        set(value) {}
    public actual  val fileTimeStampObject: Any
        get() = TODO("Not yet implemented")
    public actual  var fileFlags: Int
        get() = TODO("Not yet implemented")
        set(value) {}
    public actual  override var id: ComplexValue.Id
        get() = TODO("Not yet implemented")
        set(value) {}
    public actual  override var complexValueForeignKey: ComplexValueForeignKey
        get() = TODO("Not yet implemented")
        set(value) {}
    public actual  override val column: Column
        get() = TODO("Not yet implemented")

    public actual  override fun update() {
    }

    public actual  override fun delete() {
    }
}
