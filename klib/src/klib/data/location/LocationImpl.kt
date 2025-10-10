package klib.data.location

import kotlinx.serialization.Serializable

@Serializable
public data class LocationImpl(
    override val longitude: Double,
    override val latitude: Double,
    override val altitude: Double = 0.0,
    override val identifier: String? = null,
    override val description: String? = null,
) : Location
