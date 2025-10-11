package clib.presentation.components.map

import klib.data.location.Location
import clib.presentation.components.map.model.Marker
import clib.presentation.components.map.model.MapView
import clib.presentation.components.map.model.MapLocalization
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
public expect fun Map(
    modifier: Modifier = Modifier,
    view: MapView = MapView(),
    markers: List<Marker>? = null,
    onMarkerClick: ((Location, href: String?) -> Boolean)? = null,
    routes: List<List<Location>>? = null,
    onSelect: ((removed: Set<Location>, added: Set<Location>) -> Unit)? = null,
    localization: MapLocalization = MapLocalization(),
)
