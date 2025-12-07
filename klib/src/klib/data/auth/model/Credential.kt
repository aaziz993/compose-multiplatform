package klib.data.auth.model

import kotlinx.serialization.Serializable

@Serializable
public data class Credential(
    val username: String,
    val password: String,
)
