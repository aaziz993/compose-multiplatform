package klib.data.backup.onedrive.model

import kotlin.time.Instant
import kotlinx.serialization.Serializable

@Serializable
public data class OneDriveRefreshableAccessData(
    val expiresAt: Instant,
    val accessToken: String,
    val refreshToken: String,
)
