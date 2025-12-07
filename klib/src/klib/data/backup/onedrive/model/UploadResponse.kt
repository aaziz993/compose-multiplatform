package klib.data.backup.onedrive.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
internal data class UploadResponse(
    @SerialName("file") val file: File,
) {

    @Serializable
    data class File(
        @SerialName("hashes") val hashes: Hashes,
    ) {

        @Serializable
        data class Hashes(
            @SerialName("sha256Hash") val sha256Hash: String,
        )
    }
}
