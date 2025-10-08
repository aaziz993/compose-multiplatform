package klib.data.location

import klib.data.crud.model.entity.EntityWithMetadata
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
public data class LocationEntity(
    override val latitude: Double,
    override val longitude: Double,
    override val altitude: Double = 0.0,
    override val identifier: String? = null,
    override val description: String? = null,
    override val id: Long? = null,
    override val createdBy: String? = null,
    override val createdAt: LocalDateTime? = null,
    override val updatedBy: String? = null,
    override val updatedAt: LocalDateTime? = null,
) : Location, EntityWithMetadata<Long, LocalDateTime, LocalDateTime>
