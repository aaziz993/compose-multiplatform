package clib.presentation.components.map

import klib.data.location.Location
import clib.presentation.components.map.model.MapLocation
import clib.presentation.components.map.model.MapViewConfig
import clib.presentation.components.map.model.MapViewLocalization
import androidx.compose.runtime.Composable

@Composable
public expect fun MapView(
    config: MapViewConfig = MapViewConfig(),
    markers: List<MapLocation>? = null,
    onMarkerClick: ((Location, href: String?) -> Boolean)? = null,
    routes: List<List<Location>>? = null,
    onSelect: ((removed: Set<Location>, added: Set<Location>) -> Unit)? = null,
    localization: MapViewLocalization = MapViewLocalization(),
)
