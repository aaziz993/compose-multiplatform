package clib.presentation.components.map.model

import klib.data.location.LocationImpl
import kotlinx.serialization.Serializable

@Serializable
public data class Camera(
    val initialZoom: Int? = null,
    val initialCenter: LocationImpl? = null,
)
