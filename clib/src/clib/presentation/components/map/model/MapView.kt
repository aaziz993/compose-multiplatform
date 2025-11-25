package clib.presentation.components.map.model

import clib.presentation.components.map.tile.OpenStreetMap
import clib.presentation.components.map.tile.Tile
import clib.presentation.components.map.tile.VirtualEarth
import kotlinx.serialization.Serializable

@Serializable
public data class MapView(
    val camera: Camera = Camera(),
    val tiles: List<Tile> = listOf(OpenStreetMap(), VirtualEarth()),
    val isTilePickerEnabled: Boolean = true,
    val ornamentOptions: OrnamentOptions = OrnamentOptions(),
    val gestureOptions: GestureOptions = GestureOptions(),
    val selectTile: String = "SelectTile",
)
