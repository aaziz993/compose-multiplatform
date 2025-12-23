package klib.data.config.auth

import kotlinx.serialization.Serializable

@Serializable
public data class GoogleAuthProviderConfig(
    val webClientId: String,
    val clientSecret: String? = null
)
