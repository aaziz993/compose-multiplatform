package klib.data.auth.firebase.model

import kotlinx.serialization.Serializable

@Serializable
public data class FederatedUserId(
    val providerId: String? = null,
    val rawId: String? = null
)
