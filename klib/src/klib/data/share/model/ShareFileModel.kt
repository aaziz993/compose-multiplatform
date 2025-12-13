package klib.data.share.model

import klib.data.model.MimeType

public data class ShareFileModel(
    val mime: MimeType = MimeType.PDF,
    val fileName: String,
    val bytes: ByteArray
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as ShareFileModel

        if (mime != other.mime) return false
        if (fileName != other.fileName) return false
        if (!bytes.contentEquals(other.bytes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = mime.hashCode()
        result = 31 * result + fileName.hashCode()
        result = 31 * result + bytes.contentHashCode()
        return result
    }
}
