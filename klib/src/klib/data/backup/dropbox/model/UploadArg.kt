package klib.data.backup.dropbox.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
internal data class UploadArg(
    @SerialName("content_hash") val contentHash: String,
    @SerialName("path") val path: String,
    @SerialName("autorename") val autoRename: Boolean = false,
    @SerialName("mode") val mode: klib.data.backup.dropbox.model.DropboxUploadArg.WriteMode = WriteMode.overwrite,
    @SerialName("mute") val mute: Boolean = true,
    @SerialName("strict_conflict") val strictConflict: Boolean = false,
) {

    @Serializable
    enum class WriteMode {

        add, overwrite, update
    }
}
