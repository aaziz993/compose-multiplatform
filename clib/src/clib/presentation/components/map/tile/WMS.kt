package clib.presentation.components.map.tile

import clib.presentation.components.map.MercatorUtils
import kotlin.math.PI
import kotlin.math.pow
import kotlinx.serialization.Serializable

@Serializable
public data class WMS(
    override val minimumZoomLevel: Int,
    override val maximumZoomLevel: Int,
    override val totalMapZoom: Int,
    override val baseURL: String,
    val layers: String,
    val styles: String = "",
    val tileBgColor: String? = "0xAFDAF6",
    val tileFormat: String = "image/jpeg",
    val srs: String = "EPSG:4326",
    override val tileSize: Int = 255,
) : Tile() {

    override val name: String
        get() = "name not provided"
    override val xr2l: Boolean
        get() = true
    override val yt2b: Boolean
        get() = true
    override val xParam: String
        get() = "x"
    override val yParam: String
        get() = "y"
    override val zParam: String
        get() = "zoom"

    override fun getTileUrl(x: Int, y: Int, zoom: Int): String {
        var zoom = zoom
        val tileSize = getTileSize(zoom)
        zoom = totalMapZoom - zoom
        val z = 2.0.pow(zoom.toDouble() - 1).toInt()

        val m = x - z
        val n = z - 1 - y

        val tilesPerDimension = 2.0.pow(zoom.toDouble()).toInt()

        val radius: Double = (tileSize * tilesPerDimension) / (2 * PI)
        val ulx: Double = MercatorUtils.xToLong(m * tileSize, radius)
        val uly: Double = MercatorUtils.yToLat(n * tileSize, radius)
        var lrx: Double = MercatorUtils.xToLong((m + 1) * tileSize, radius)
        val lry: Double = MercatorUtils.yToLat((n + 1) * tileSize, radius)

        if (lrx < ulx) lrx = -lrx

        val bbox = "$ulx,$uly,$lrx,$lry"
        return "$baseURL?version=1.1.1&request=GetMap&layers=$layers&format=$tileFormat&bbox=$bbox&width=$tileSize&height=$tileSize&srs=$srs&styles=$styles${if (tileBgColor == null) "" else "&bgcolor=$tileBgColor"}"
    }
}
