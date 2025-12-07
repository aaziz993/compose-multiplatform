package klib.data.auth.firebase.model

import kotlinx.serialization.Serializable

@Serializable
public data class ResetPasswordResponse(
    val email: String,
    val requestType: String // Should be "PASSWORD_RESET".
)
