package klib.data.auth.firebase.model

import kotlinx.serialization.Serializable

@Serializable
public data class CreateAuthUriResponse(
    val allProviders: List<String>,
    val registered: Boolean,
)
