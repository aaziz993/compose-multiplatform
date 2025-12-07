package klib.data.auth.firebase.model

import kotlinx.serialization.Serializable

@Serializable
public data class SendOobResponse(
    val email: String,
)
