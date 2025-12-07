package klib.data.auth.firebase.model

import kotlinx.serialization.Serializable

@Serializable
public data class UserMetadata(
    val creationTimestamp: Long,
    val lastRefreshTimestamp: Long,
    val lastSignInTimestamp: Long
)
