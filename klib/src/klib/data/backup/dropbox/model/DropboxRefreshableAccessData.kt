package klib.data.backup.dropbox.model

import kotlin.time.Instant
import kotlinx.serialization.Serializable

@Serializable
public data class DropboxRefreshableAccessData(
    val expiresAt: Instant,
    val accessToken: String,
    val refreshToken: String,
)
