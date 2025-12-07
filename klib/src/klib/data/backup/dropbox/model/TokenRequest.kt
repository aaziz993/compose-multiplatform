package klib.data.backup.dropbox.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class TokenRequest(
    @SerialName("client_id")
    val clientId: String,
    val code: String,
    @SerialName("code_verifier")
    val codeVerifier: String,
    @SerialName("grant_type")
    val grantType: String = "authorization_code",
)
