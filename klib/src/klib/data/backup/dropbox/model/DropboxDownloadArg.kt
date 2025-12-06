package klib.data.backup.dropbox.model

import klib.data.backup.dropbox.BACKUP_PATH
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class DropboxDownloadArg(
    @SerialName("path") val path: String = BACKUP_PATH,
)
