package klib.data.auth.firebase.model

import kotlinx.serialization.Serializable

@Serializable
public data class DeleteRequest(
    val idToken: String? = null,
)
