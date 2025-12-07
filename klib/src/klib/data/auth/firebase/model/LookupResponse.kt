package klib.data.auth.firebase.model

import kotlinx.serialization.Serializable

@Serializable
public data class LookupResponse(
    val users: List<UserRecord>? = null
)
