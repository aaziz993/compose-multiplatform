package clib.presentation.components.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import clib.presentation.components.map.model.Marker
import clib.presentation.components.map.model.MapView
import klib.data.location.Location

@Composable
public actual fun Map(
    modifier: Modifier,
    markers: List<Marker>?,
    onMarkerClick: ((Location, String?) -> Boolean)?,
    routes: List<List<Location>>?,
    onSelect: ((Set<Location>, Set<Location>) -> Unit)?,
    view: MapView,
) {
}
