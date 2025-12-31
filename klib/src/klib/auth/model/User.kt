package klib.auth.model

import kotlinx.serialization.Serializable

@Serializable
public data class User(
    val username: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val imageUrl: String? = null,
    val roles: Set<String> = emptySet(),
    val isVerified: Boolean = false,
    val attributes: Map<String, List<String>> = emptyMap()
)
