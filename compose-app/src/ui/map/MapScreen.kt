package ui.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import clib.presentation.components.map.Map
import clib.presentation.components.map.model.MapView
import clib.presentation.components.map.model.MapLocalization
import clib.presentation.components.map.model.Marker
import clib.presentation.components.navigation.viewmodel.NavigationAction
import klib.data.location.LocationImpl
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.navigation.presentation.Map

@Composable
public fun MapScreen(
    modifier: Modifier = Modifier,
    route: Map = Map,
    onNavigationAction: (NavigationAction) -> Unit = {},
) {
    val center = LocationImpl(2.3522, 48.8566)
    val markers = listOf(
        Marker(LocationImpl(2.3522, 48.8566, description = "Paris")),
        Marker(LocationImpl(-0.1278, 51.5074, description = "London")),
        Marker(LocationImpl(-74.0060, 40.7128, description = "New York")),
    )

    Map(
        modifier = Modifier.fillMaxSize(),
        config = MapView(
            initialCenter = center,
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
        localization = MapLocalization(
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
