package clib.data.location

import clib.presentation.components.map.model.Marker
import clib.presentation.components.map.model.SwingWaypoint
import java.awt.Dimension
import klib.data.location.Location
import klib.data.location.LocationImpl
import org.jxmapviewer.viewer.GeoPosition

public fun Location.toSwingWaypoint(
    size: Dimension = Dimension(30, 40),
    offset: Dimension = Dimension(-15, -40),
    image: ByteArray = Marker.image,
    onHyperLinkClick: ((Location, href: String) -> Boolean)? = null
): SwingWaypoint = SwingWaypoint(toGeoPosition(), identifier, description, size, offset, image, onHyperLinkClick)

public fun SwingWaypoint.toLocation(): Location =
    LocationImpl(position.latitude, position.longitude, 0.0, id, description)

public fun Location.toGeoPosition(): GeoPosition = GeoPosition(latitude, longitude)
