package ui.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import clib.presentation.components.map.Map
import clib.presentation.components.map.model.MapView
import clib.presentation.components.map.model.Marker
import clib.presentation.navigation.NavigationAction
import compose_app.generated.resources.Res
import compose_app.generated.resources.google_map
import compose_app.generated.resources.open_street_map
import compose_app.generated.resources.select_tile
import compose_app.generated.resources.virtual_earth_map
import klib.data.location.LocationImpl
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.tooling.preview.Preview
import clib.presentation.components.map.model.Camera
import clib.presentation.components.map.model.GestureOptions
import clib.presentation.components.map.tile.GoogleMaps
import clib.presentation.components.map.tile.OpenStreetMap
import clib.presentation.components.map.tile.VirtualEarth
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
        markers = markers,
        onMarkerClick = { location, href ->
            // Navigate somewhere when a marker is clicked
            true
        },
        onSelect = { removed, added ->
            println("Selection changed: added=$added removed=$removed")
        },
        routes = null,
        view = MapView(
            camera = Camera(
                initialCenter = center,
                initialZoom = 4,
            ),
            gestureOptions = GestureOptions(),
            selectTile = stringResource(Res.string.select_tile),
            tiles = listOf(
                OpenStreetMap(stringResource(Res.string.open_street_map)),
                VirtualEarth(stringResource(Res.string.virtual_earth_map)),
                GoogleMaps(name = stringResource(Res.string.google_map), key = ""),
            ),
        ),
    )
}

@Preview
@Composable
public fun PreviewMapScreen(): Unit = MapScreen()
