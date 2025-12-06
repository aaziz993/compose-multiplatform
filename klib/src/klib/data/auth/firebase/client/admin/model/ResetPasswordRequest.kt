package klib.data.auth.firebase.client.admin.model

import kotlinx.serialization.Serializable

@Serializable
public data class ResetPasswordRequest(
    val oobCode: String,
    val newPassword: String? = null
)
