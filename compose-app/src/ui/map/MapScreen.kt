package ui.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import clib.presentation.components.map.MapView
import clib.presentation.components.map.model.MapViewConfig
import clib.presentation.components.map.model.MapViewLocalization
import clib.presentation.components.map.model.Marker
import clib.presentation.components.navigation.model.NavigationRoute
import clib.presentation.components.navigation.viewmodel.NavigationAction
import compose_app.generated.resources.Res
import compose_app.generated.resources.select_tile
import klib.data.location.LocationImpl
import kotlin.String
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.navigation.presentation.Map
import ui.navigation.presentation.NavRoute

@Composable
public fun MapScreen(
    modifier: Modifier = Modifier,
    route: Map = Map,
    onNavigationAction: (NavigationAction) -> Unit = {},
) {
    val center = LocationImpl(48.8566, 2.3522)
    val markers = listOf(
        Marker(LocationImpl(48.8566, 2.3522, description = "Paris")),
        Marker(LocationImpl(51.5074, -0.1278, description = "London")),
        Marker(LocationImpl(40.7128, -74.0060, description = "New York")),
    )

    MapView(
        modifier = Modifier.fillMaxSize(),
        config = MapViewConfig(
            initialCenter = LocationImpl(48.8566, 2.3522),
            initialZoom = 4,
            movable = true,
            zoomable = true,
        ),
        markers = markers,
        onMarkerClick = { location, href ->
            // Navigate somewhere when a marker is clicked
            true
        },
        onSelect = { removed, added ->
            println("Selection changed: added=$added removed=$removed")
        },
        routes = null,
        localization = MapViewLocalization(
            selectTile = "SelectTile",
            virtualEarthMap = "VirtualEarth Map",
            openStreetMap = "OpenStreet Map",
            googleMap = "Google Map",
        ),
    )
}

@Preview
@Composable
public fun PreviewMapScreen(): Unit = MapScreen()
