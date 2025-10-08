package clib.presentation.components.map

import androidx.compose.runtime.Composable
import clib.presentation.components.map.model.MapLocation
import clib.presentation.components.map.model.MapViewConfig
import clib.presentation.components.map.model.MapViewLocalization
import klib.data.location.Location

@Composable
public actual fun MapView(
    config: MapViewConfig,
    markers: List<MapLocation>?,
    onMarkerClick: ((Location, String?) -> Boolean)?,
    routes: List<List<Location>>?,
    onSelect: ((Set<Location>, Set<Location>) -> Unit)?,
    localization: MapViewLocalization
) {
}
