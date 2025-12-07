package klib.data.auth.firebase.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class SignInResponse(
    @SerialName("idToken")
    override val token: String,
    override val email: String,
    override val refreshToken: String,
    override val expiresIn: String,
    override val localId: String,
    val registered: Boolean,
) : SignResponse
