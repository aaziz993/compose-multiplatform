package klib.data.auth.firebase.model

import kotlinx.serialization.Serializable

@Serializable
    public data class SignInWithCustomTokenRequest(
        val token: String,
        val returnSecureToken: Boolean = true, // should always be true
    )
