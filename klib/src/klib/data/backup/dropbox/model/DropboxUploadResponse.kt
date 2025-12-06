package klib.data.backup.dropbox.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
internal data class DropboxUploadResponse(
    @SerialName("content_hash") val contentHash: String,
)
