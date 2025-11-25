package clib.presentation.components.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import clib.data.location.toPosition
import clib.presentation.components.map.model.GestureOptions
import clib.presentation.components.map.model.MapView
import clib.presentation.components.map.model.Marker
import clib.presentation.components.map.model.toFeature
import klib.data.location.Location
import klib.data.type.collections.takeIfNotEmpty
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.compose.camera.rememberCameraState
import org.maplibre.compose.expressions.dsl.Feature.get
import org.maplibre.compose.expressions.dsl.const
import org.maplibre.compose.expressions.dsl.image
import org.maplibre.compose.layers.LineLayer
import org.maplibre.compose.layers.SymbolLayer
import org.maplibre.compose.map.MapOptions
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.map.OrnamentOptions
import org.maplibre.compose.material3.DisappearingCompassButton
import org.maplibre.compose.material3.DisappearingScaleBar
import org.maplibre.compose.material3.ExpandingAttributionButton
import org.maplibre.compose.sources.GeoJsonData
import org.maplibre.compose.sources.rememberGeoJsonSource
import org.maplibre.compose.style.BaseStyle
import org.maplibre.compose.style.rememberStyleState
import org.maplibre.compose.util.ClickResult
import org.maplibre.spatialk.geojson.FeatureCollection
import org.maplibre.spatialk.geojson.LineString
import org.maplibre.spatialk.geojson.Position

@Composable
public actual fun Map(
    modifier: Modifier,
    markers: List<Marker>?,
    onMarkerClick: ((Location, href: String?) -> Boolean)?,
    routes: List<List<Location>>?,
    onSelect: ((removed: Set<Location>, added: Set<Location>) -> Unit)?,
    view: MapView,
) {

    var selectedTile by remember { mutableStateOf(view.tiles.first()) }

    val cameraState = rememberCameraState(
        CameraPosition(
            target = view.camera.initialCenter?.toPosition() ?: Position(0.0, 0.0),
            zoom = view.camera.initialZoom?.toDouble() ?: 1.0,
        ),
    )
    val styleState = rememberStyleState()

    val selectedMarkers = remember { mutableStateSetOf<Marker>() }

    Box(Modifier.fillMaxSize()) {
        MaplibreMap(
            modifier = modifier,
            baseStyle = BaseStyle.Uri(selectedTile.baseURL),
            cameraState = cameraState,
            zoomRange = 0f..20f,
            pitchRange = 0f..60f,
            styleState = styleState,
            options = MapOptions(
                gestureOptions = view.gestureOptions.toGestureOptions(),
                ornamentOptions =
                    if (view.ornamentOptions.isAttributionEnabled) OrnamentOptions.AllEnabled else OrnamentOptions.AllDisabled,
            ),
        ) {
            markers?.takeIfNotEmpty()?.let { markers ->
                val features = FeatureCollection(markers.map(Marker::toFeature))
                val markerSource = rememberGeoJsonSource(GeoJsonData.Features(features))

                SymbolLayer(
                    id = "marker-layer",
                    source = markerSource,
                    iconImage = image("image"),
                    iconOffset = get("offset").cast(),
                    iconAllowOverlap = const(true),
                    onClick = { features ->
                        if (onMarkerClick == null) ClickResult.Pass
                        else {
                            features.forEach { feature ->
                                onMarkerClick(markers.single { it.location.identifier == feature.id?.content }.location, null)
                            }
                            ClickResult.Consume
                        }
                    },
                    onLongClick = { features ->
                        if (onSelect == null) ClickResult.Pass
                        else {
                            val (left, right) = features.map { feature -> markers.single { marker -> marker.location.identifier == feature.id?.content } }.partition { marker ->
                                if (marker in selectedMarkers) {
                                    selectedMarkers -= marker
                                    true
                                }
                                else {
                                    selectedMarkers += marker
                                    false
                                }
                            }
                            onSelect(left.map(Marker::location).toSet(), right.map(Marker::location).toSet())
                            ClickResult.Consume
                        }
                    },
                )
            }

            routes?.takeIfNotEmpty()?.forEachIndexed { index, route ->
                val lineString = LineString(route.map(Location::toPosition))
                val routeSource = rememberGeoJsonSource(GeoJsonData.Features(lineString))

                LineLayer(
                    id = "route-layer-$index",
                    source = routeSource,
                    color = const(Color.Blue),
                    width = const(3.dp),
                    onClick = { features ->
                        ClickResult.Consume
                    },
                )
            }
        }

        Box(modifier = Modifier.fillMaxSize().padding(8.dp)) {
            DisappearingScaleBar(
                metersPerDp = cameraState.metersPerDpAtTarget,
                zoom = cameraState.position.zoom,
                modifier = Modifier.align(Alignment.TopStart),
            )
            TilePicker(
                tiles = view.tiles,
                selectedTile = view.tiles.first(),
                onTileSelected = { tile -> selectedTile = tile },
                modifier = Modifier.align(Alignment.TopCenter),
            )
            DisappearingCompassButton(cameraState, modifier = Modifier.align(Alignment.TopEnd))
            ExpandingAttributionButton(
                cameraState = cameraState,
                styleState = styleState,
                modifier = Modifier.align(Alignment.BottomEnd),
                contentAlignment = Alignment.BottomEnd,
            )
        }
    }
}

private fun GestureOptions.toGestureOptions(): org.maplibre.compose.map.GestureOptions = when {
    !isMoveEnabled && isZoomEnabled && isRotateEnabled -> org.maplibre.compose.map.GestureOptions.PositionLocked
    isMoveEnabled && isZoomEnabled && !isRotateEnabled -> org.maplibre.compose.map.GestureOptions.RotationLocked
    isZoomEnabled && !isMoveEnabled && !isRotateEnabled -> org.maplibre.compose.map.GestureOptions.ZoomOnly
    !isZoomEnabled && !isMoveEnabled && !isRotateEnabled -> org.maplibre.compose.map.GestureOptions.AllDisabled
    else -> org.maplibre.compose.map.GestureOptions.Standard
}
