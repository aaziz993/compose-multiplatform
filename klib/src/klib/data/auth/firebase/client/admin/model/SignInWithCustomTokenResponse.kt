package klib.data.auth.firebase.client.admin.model

import klib.data.auth.client.bearer.model.BearerToken
import kotlinx.serialization.Serializable

@Serializable
public data class SignInWithCustomTokenResponse(
    override val idToken: String,
    override val refreshToken: String,
    val expiresIn: String,
) : BearerToken
