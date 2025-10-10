package clib.data.location

import io.github.dellisd.spatialk.geojson.Position
import klib.data.location.Location
import klib.data.location.LocationImpl

public fun Location.toPosition(): Position = Position(longitude, latitude)

public fun Position.toLocation(): Location = LocationImpl(longitude, latitude)
