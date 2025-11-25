package clib.presentation.components.map.model

import clib.data.location.toSwingWaypoint
import clib.data.type.toDimension

public fun Marker.toSwingWaypoint(): SwingWaypoint = location.toSwingWaypoint(
    size.toDimension(),
    offset.toDimension(),
    image,
)
