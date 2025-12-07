package klib.data.backup.onedrive.model

import klib.data.auth.model.BearerToken
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class TokenResponse(
    @SerialName("access_token")
    override val accessToken: String,
    @SerialName("expires_in")
    val expiresIn: Int, // In seconds.
    @SerialName("token_type")
    val tokenType: String,
    @SerialName("refresh_token")
    override val refreshToken: String?,
) : BearerToken
