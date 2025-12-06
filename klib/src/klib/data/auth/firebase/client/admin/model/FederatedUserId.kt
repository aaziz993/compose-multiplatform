package klib.data.auth.firebase.client.admin.model

import kotlinx.serialization.Serializable

@Serializable
public data class FederatedUserId(
    val providerId: String? = null,
    val rawId: String? = null
)
