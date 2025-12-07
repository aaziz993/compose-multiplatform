package klib.data.auth.firebase.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class TokenRequest(
    @SerialName("refresh_token")
    val refreshToken: String,
    @SerialName("grant_type")
    val grantType: String = "refresh_token", // should always be refresh_token
)
