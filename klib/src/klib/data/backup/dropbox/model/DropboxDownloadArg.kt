package klib.data.backup.dropbox.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class DropboxDownloadArg(
    @SerialName("path") val path: String,
)
