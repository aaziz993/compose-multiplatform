package klib.auth.keycloak.model

import klib.auth.model.identity.User
import kotlinx.serialization.Serializable

private const val USER_PHONE_ATTRIBUTE_KEY: String = "phone"
private const val USER_IMAGE_ATTRIBUTE_KEY: String = "image"

@Serializable
public data class UserRepresentation(
    val access: Access? = null,
    val attributes: Map<String, List<String>>? = null,
    val clientConsents: Set<UserConsentRepresentation>? = null,
    val clientRoles: Set<RoleRepresentation>? = null,
    val createdTimestamp: Long? = null,
    val credentials: Set<CredentialRepresentation>? = null,
    val disableableCredentialTypes: Set<String>? = null,
    val email: String? = null,
    val emailVerified: Boolean? = null,
    val enabled: Boolean? = null,
    val federatedIdentities: Set<FederatedIdentityRepresentation>? = null,
    val federationLink: String? = null,
    val firstName: String? = null,
    val groups: Set<String>? = null,
    val id: String? = null,
    val lastName: String? = null,
    val notBefore: Long? = null,
    val origin: String? = null,
    val realmRoles: Set<String>? = null,
    val requiredActions: Set<String>? = null,
    val self: String? = null,
    val serviceAccountClientId: String? = null,
    val username: String? = null,
    val totp: Boolean? = null,
)

public fun User.toUserRepresentation(id: String? = null): UserRepresentation = UserRepresentation(
    username = username,
    firstName = firstName,
    lastName = lastName,
    email = email,
    realmRoles = roles,
    attributes = if (phone != null || image != null)
        (attributes.orEmpty()) + buildMap {
            phone?.let { this[USER_PHONE_ATTRIBUTE_KEY] = listOf(it) }
            image?.let { this[USER_IMAGE_ATTRIBUTE_KEY] = listOf(it) }
        }
    else attributes,
    id = id,
)

public fun UserRepresentation.toUser(): User = User(
    username,
    firstName,
    lastName,
    attributes?.get(USER_PHONE_ATTRIBUTE_KEY)?.get(0),
    email,
    attributes?.get(USER_IMAGE_ATTRIBUTE_KEY)?.get(0),
    realmRoles,
    attributes?.toMutableMap()?.apply {
        remove(USER_PHONE_ATTRIBUTE_KEY)
        remove(USER_IMAGE_ATTRIBUTE_KEY)
    },
)
