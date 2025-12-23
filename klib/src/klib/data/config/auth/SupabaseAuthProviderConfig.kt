package klib.data.config.auth

import kotlinx.serialization.Serializable

@Serializable
public data class SupabaseAuthProviderConfig(
    val config: KMAuthConfigSerial,
    val redirectUrl: String? = null,
)
