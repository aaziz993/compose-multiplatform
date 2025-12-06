package klib.data.backup.dropbox.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class DownloadResponse(
    @SerialName("content_hash") val contentHash: String,
)
