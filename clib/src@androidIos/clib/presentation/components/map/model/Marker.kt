package clib.presentation.components.map.model

import androidx.compose.ui.graphics.decodeToImageBitmap
import androidx.compose.ui.unit.dp
import clib.data.location.toPosition
import clib.data.type.resize
import kotlinx.serialization.json.JsonPrimitive
import org.maplibre.compose.expressions.dsl.image
import org.maplibre.compose.expressions.dsl.offset
import org.maplibre.spatialk.geojson.Feature
import org.maplibre.spatialk.geojson.Point

public fun Marker.toFeature(): Feature<Point, Map<String, Any>> {
    val image = image.decodeToImageBitmap().resize(size.width, size.height)

    return Feature(
        Point(location.toPosition()),
        mapOf(
            "offset" to offset(offset.x.dp, offset.y.dp),
            "image" to image(image),
        ),
        JsonPrimitive(location.identifier),
    )
}


