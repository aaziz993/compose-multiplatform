package clib.presentation.components.map.tile

import kotlin.math.PI
import kotlin.math.atan
import kotlin.math.pow
import kotlin.math.sinh

public data class GoogleMaps(
    override val name: String = "Google Maps",
    override val baseURL: String = "https://maps.googleapis.com/maps/api/staticmap",
    val key: String,
) : Tile() {

    override val minimumZoomLevel: Int
        get() = 1
    override val maximumZoomLevel: Int
        get() = MAX - 2
    override val totalMapZoom: Int
        get() = MAX
    override val tileSize: Int
        get() = TILE_SIZE
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
        var zoom = zoom
        zoom = totalMapZoom - zoom

        val xTile = x + 0.5
        val yTile = y + 0.5

        val n = 2.0.pow(zoom.toDouble())
        val lonDeg = ((xTile / n) * 360.0) - 180.0
        val latRad = atan(sinh(PI * (1 - 2 * yTile / n)))
        val latDeg: Double = (latRad * 180.0) / PI

        return "$baseURL?center=$latDeg,$lonDeg&zoom=$zoom&key=$key&maptype=roadmap&size=${TILE_SIZE}x$TILE_SIZE"
    }

    override val attribution: String
        get() = "\u00A9 Google"

    override val license: String
        get() = "https://www.google.com/intl/en_US/help/terms_maps/"

    public companion object Companion {

        private const val MAX: Int = 19

        /**
         * Currently only 256x256 works - see https://github.com/msteiger/jxmapviewer2/issues/62
         */
        private const val TILE_SIZE: Int = 256
    }
}
