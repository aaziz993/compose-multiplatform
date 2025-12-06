package klib.data.auth.keycloak.client.admin.model

import kotlinx.serialization.Serializable

@Serializable
public data class FederatedIdentityRepresentation(
    val identityProvider: String? = null,
    val userId: String? = null,
    val userName: String? = null,
)
