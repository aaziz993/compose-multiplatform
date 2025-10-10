package clib.presentation.components.map.model

import clib.data.location.toPosition
import io.github.dellisd.spatialk.geojson.Feature
import io.github.dellisd.spatialk.geojson.Point

public fun Marker.toFeature(): Feature = Feature(
    geometry = Point(location.toPosition()),
    id = location.identifier,
)
