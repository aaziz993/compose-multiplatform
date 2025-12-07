package klib.data.auth.firebase.model

import kotlinx.serialization.Serializable

@Serializable
public data class ResetPasswordRequest(
    val oobCode: String,
    val newPassword: String? = null
)
