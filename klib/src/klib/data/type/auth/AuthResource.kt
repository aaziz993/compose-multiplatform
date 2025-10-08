package klib.data.type.auth

import klib.data.type.collections.contains
import kotlinx.serialization.Serializable

@Serializable
public data class AuthResource(
    val providers: List<String?> = listOf(null),
    val roles: Set<String> = emptySet(),
    val allRoles: Boolean? = false,
) {

    public fun validate(roles: Set<String>): Boolean = this.roles.contains(roles, allRoles)

    public fun validate(provider: String? = null, user: User?): Boolean =
        provider in providers && user?.roles?.let(::validate) == true
}
