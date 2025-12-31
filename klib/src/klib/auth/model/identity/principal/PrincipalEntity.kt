package klib.auth.model.identity.principal

import klib.data.entity.EntityWithMetadata
import klib.data.entity.annotation.Json
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
public data class PrincipalEntity(
    override val id: Long? = null,
    val username: String,
    val password: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val image: String? = null,
    val roles: Set<String>? = null,
    @Json
    var attributes: Map<String, List<String>>? = null,
    override val createdBy: String? = null,
    override val createdAt: LocalDateTime? = null,
    override val updatedBy: String? = null,
    override val updatedAt: LocalDateTime? = null,
) : EntityWithMetadata<Long, LocalDateTime, LocalDateTime>
