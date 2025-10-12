package clib.presentation.components.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.graphics.Color
import clib.data.location.toGeoPosition
import clib.data.location.toLocation
import clib.presentation.components.map.model.JxMapView
import clib.presentation.components.map.model.MapView
import clib.presentation.components.map.model.MapLocalization
import clib.presentation.components.map.model.Marker
import clib.presentation.components.map.model.SwingWaypoint
import clib.presentation.components.map.model.toSwingWaypoint
import klib.data.location.Location

@Composable
public actual fun Map(
    modifier: Modifier,
    view: MapView,
    markers: List<Marker>?,
    onMarkerClick: ((Location, href: String?) -> Boolean)?,
    routes: List<List<Location>>?,
    onSelect: ((removed: Set<Location>, added: Set<Location>) -> Unit)?,
    localization: MapLocalization,
): Unit = SwingPanel(
    modifier = modifier,
    background = Color.Transparent,
    factory = {
        JxMapView(
            initialCenter = view.initialCenter?.toGeoPosition(),
            initialZoom = view.initialZoom,
            zoomable = view.zoomable,
            movable = view.movable,
            tilePicker = view.tilePicker,
            googleApiKey = view.googleApiKey,
            markers = markers?.map(Marker::toSwingWaypoint)?.toSet(),
            routes = routes?.map { path ->
                path.map { it.toGeoPosition() }
            },
            onSelect = { removed, added ->
                onSelect?.invoke(
                    removed.map(SwingWaypoint::toLocation).toSet(),
                    added.map(SwingWaypoint::toLocation).toSet(),
                )
            },
            localization = localization,
        )
    },
)
