package klib.data.auth.firebase.model

import klib.data.auth.client.model.BearerToken
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class SignInWithCustomTokenResponse(
    @SerialName("idToken")
    override val accessToken: String,
    override val refreshToken: String,
    val expiresIn: String,
) : BearerToken
