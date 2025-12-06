package klib.data.auth.firebase.client.admin.model

import klib.data.auth.client.model.BearerToken
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class TokenResponse(
    @SerialName("id_token")
    override val token: String,
    @SerialName("refresh_token")
    override val refreshToken: String,
    @SerialName("expires_in")
    val expiresIn: String,
    @SerialName("token_type")
    val tokenType: String, // always Bearer
    @SerialName("user_id")
    val userId: String,
    @SerialName("project_id")
    val projectId: String
) : BearerToken
