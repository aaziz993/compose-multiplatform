package clib.presentation.components.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import clib.data.location.toPosition
import clib.presentation.components.map.model.MapLocalization
import clib.presentation.components.map.model.MapView
import clib.presentation.components.map.model.Marker
import clib.presentation.components.map.model.toFeature
import io.github.dellisd.spatialk.geojson.FeatureCollection
import io.github.dellisd.spatialk.geojson.LineString
import io.github.dellisd.spatialk.geojson.Position
import klib.data.location.Location
import klib.data.type.collections.symmetricMinus
import klib.data.type.collections.takeIfNotEmpty
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.compose.camera.rememberCameraState
import org.maplibre.compose.expressions.dsl.const
import org.maplibre.compose.layers.CircleLayer
import org.maplibre.compose.layers.LineLayer
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.material3.DisappearingCompassButton
import org.maplibre.compose.material3.DisappearingScaleBar
import org.maplibre.compose.material3.ExpandingAttributionButton
import org.maplibre.compose.sources.GeoJsonData
import org.maplibre.compose.sources.rememberGeoJsonSource
import org.maplibre.compose.style.BaseStyle
import org.maplibre.compose.style.rememberStyleState
import org.maplibre.compose.util.ClickResult

@Composable
public actual fun Map(
    modifier: Modifier,
    view: MapView,
    markers: List<Marker>?,
    onMarkerClick: ((Location, href: String?) -> Boolean)?,
    routes: List<List<Location>>?,
    onSelect: ((removed: Set<Location>, added: Set<Location>) -> Unit)?,
    localization: MapLocalization,
) {

    val cameraState = rememberCameraState(
        CameraPosition(
            target = view.initialCenter?.toPosition() ?: Position(0.0, 0.0),
            zoom = view.initialZoom?.toDouble() ?: 5.0,
        ),
    )
    val styleState = rememberStyleState()

    var selectedMarkers by remember { mutableStateOf<Set<Marker>>(emptySet()) }

    Box(Modifier.fillMaxSize()) {
        MaplibreMap(
            modifier = modifier,
            baseStyle = BaseStyle.Uri("https://tiles.openfreemap.org/styles/liberty"),
            cameraState = cameraState,
            zoomRange = 0f..20f,
            pitchRange = 0f..60f,
        ) {
            markers?.takeIfNotEmpty()?.let { markers ->
                val features = FeatureCollection(markers.map(Marker::toFeature))
                val markerSource = rememberGeoJsonSource(GeoJsonData.Features(features))

                CircleLayer(
                    id = "marker-layer",
                    source = markerSource,
                    radius = const(6.dp),
                    color = const(Color.Red),
                    onClick = { features ->
                        if (onMarkerClick == null) ClickResult.Pass
                        else {
                            features.forEach { feature ->
                                onMarkerClick(markers.single { it.location.identifier == feature.id }.location, null)
                            }
                            ClickResult.Consume
                        }
                    },
                    onLongClick = { features ->
                        if (onSelect == null) ClickResult.Pass
                        else {
                            val (left, right) = selectedMarkers symmetricMinus features.map { feature -> markers.single { marker -> marker.location.identifier == feature.id } }.toSet()
                            selectedMarkers = selectedMarkers - left + right
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
