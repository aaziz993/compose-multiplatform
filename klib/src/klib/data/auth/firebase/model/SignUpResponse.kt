package klib.data.auth.firebase.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class SignUpResponse(
    @SerialName("idToken")
    override val accessToken: String,
    override val email: String,
    override val refreshToken: String,
    override val expiresIn: String,
    override val localId: String,
) : SignResponse



