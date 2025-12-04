package klib.data.auth.model

import kotlinx.serialization.Serializable

@Serializable
public data class Auth(
    val provider: String? = null,
    val user: User? = null,
)
