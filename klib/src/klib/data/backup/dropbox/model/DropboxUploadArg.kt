package klib.data.backup.dropbox.model

import klib.data.backup.dropbox.BACKUP_PATH
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
internal data class DropboxUploadArg(
    @SerialName("content_hash") val contentHash: String,
    @SerialName("path") val path: String = BACKUP_PATH,
    @SerialName("autorename") val autoRename: Boolean = false,
    @SerialName("mode") val mode: WriteMode = WriteMode.overwrite,
    @SerialName("mute") val mute: Boolean = true,
    @SerialName("strict_conflict") val strictConflict: Boolean = false,
) {

    @Serializable
    enum class WriteMode {

        add, overwrite, update
    }
}
