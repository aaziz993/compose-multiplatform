package clib.data.location

import klib.data.location.Location
import klib.data.location.LocationImpl
import org.maplibre.spatialk.geojson.Position

public fun Location.toPosition(): Position = Position(longitude, latitude)

public fun Position.toLocation(): Location = LocationImpl(longitude, latitude)
