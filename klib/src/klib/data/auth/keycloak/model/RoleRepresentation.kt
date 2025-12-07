package klib.data.auth.keycloak.model

import klib.data.type.serialization.serializers.collections.SerializableAnyMap
import kotlinx.serialization.Serializable

@Serializable
public data class RoleRepresentation(
    public val attributes: SerializableAnyMap? = null,
    val clientRole: Boolean? = null,
    val composite: Boolean? = null,
    val containerId: String? = null,
    val description: String? = null,
    val id: String? = null,
    val name: String? = null,
)
