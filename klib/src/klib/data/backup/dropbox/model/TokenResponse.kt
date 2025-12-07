package klib.data.backup.dropbox.model

import klib.data.auth.client.model.BearerToken
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

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
