package clib.presentation.components.map.tile

import kotlinx.serialization.Serializable

@Serializable
public class OpenStreetMap(
    override val name: String = "OpenStreetMap",

    override val baseURL: String = LIBERTY) : Tile() {

    override val minimumZoomLevel: Int
        get() = 0
    override val maximumZoomLevel: Int
        get() = MAX_ZOOM
    override val totalMapZoom: Int
        get() = MAX_ZOOM
    override val tileSize: Int
        get() = 256
    override val xr2l: Boolean
        get() = true
    override val yt2b: Boolean
        get() = true
    override val xParam: String
        get() = "x"
    override val yParam: String
        get() = "y"
    override val zParam: String
        get() = "z"

    override fun getTileUrl(x: Int, y: Int, zoom: Int): String {
        val invZoom = MAX_ZOOM - zoom
        return "$baseURL/$invZoom/$x/$y.png"
    }

    override val attribution: String
        get() = "\u00A9 OpenStreetMap contributors"

    override val license: String
        get() = "Creative Commons Attribution-ShareAlike 2.0"

    public companion object Companion {

        private const val MAX_ZOOM: Int = 19

        public const val POSITRON: String = "https://tiles.openfreemap.org/styles/positron"
        public const val BRIGHT: String = "https://tiles.openfreemap.org/styles/bright"
        public const val LIBERTY: String = "https://tiles.openfreemap.org/styles/liberty"
    }
}
