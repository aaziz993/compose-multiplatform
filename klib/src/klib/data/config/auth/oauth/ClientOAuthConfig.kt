package klib.data.config.auth.oauth

import kotlinx.serialization.Serializable

@Serializable
public data class ClientOAuthConfig(
    override val provider: String,
    override val address: String,
    override val realm: String,
    override val clientId: String,
) : OAuthConfig
