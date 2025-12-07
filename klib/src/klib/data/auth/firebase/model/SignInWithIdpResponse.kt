package klib.data.auth.firebase.model

import klib.data.auth.client.model.BearerToken
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class SignInWithIdpResponse(
    val federatedId: String,
    val providerId: String,
    val localId: String,
    val emailVerified: Boolean,
    val email: String,
    val oauthIdToken: String? = null,
    val oauthAccessToken: String? = null,
    val oauthTokenSecret: String? = null,
    val rawUserInfo: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val fullName: String? = null,
    val displayName: String? = null,
    val photoUrl: String? = null,
    @SerialName("idToken")
    override val accessToken: String,
    override val refreshToken: String,
    val expiresIn: String
) : BearerToken
