package clib.presentation.components.map.model

import klib.data.location.LocationImpl
import kotlinx.serialization.Serializable

@Serializable
public data class MapView(
    val initialZoom: Int? = null,
    val initialCenter: LocationImpl? = null,
    val zoomable: Boolean = true,
    val movable: Boolean = true,
    val tilePicker: Boolean = true,
    val googleApiKey: String? = null,
    val selectTile: String = "SelectTile",
    val virtualEarthMapTile: String = "VirtualEarth Map",
    val openStreetMapTile: String = "OpenStreet Map",
    val googleMapTile: String = "Google Map"
)
