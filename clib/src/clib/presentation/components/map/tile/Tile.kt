package clib.presentation.components.map.tile

import kotlinx.serialization.Serializable

/**
 * Creates a new instance of Tile. Note that Tile should be considered invariate, meaning that
 * subclasses should ensure all of the properties stay the same after the class is constructed. Returning different
 * values of getTileSize() for example is considered an error and may result in unexpected behavior.
 * @param minimumZoomLevel The minimum zoom level.
 * @param maximumZoomLevel the maximum zoom level.
 * @param totalMapZoom the top zoom level, essentially the height of the pyramid.
 * @param tileSize the size of the tiles in pixels (must be square).
 * @param xr2l if the x goes r to l (is this backwards?).
 * @param yt2b if the y goes top to bottom.
 * @param baseURL the base url for grabbing tiles.
 * @param xParam the x parameter for the tile url.
 * @param yParam the y parameter for the tile url.
 * @param zParam the z parameter for the tile url.
 */
/*
 * @param xr2l true if tile x is measured from the far left of the map to the far right, or else false if based on
 * the center line.
 * @param yt2b true if tile y is measured from the top (north pole) to the bottom (south pole) or else false if
 * based on the equator.
 */
@Serializable
public abstract class Tile {

    public abstract val name: String
    public abstract val minimumZoomLevel: Int
    public abstract val maximumZoomLevel: Int
    public abstract val totalMapZoom: Int
    public abstract val tileSize: Int
    public abstract val xr2l: Boolean
    public abstract val yt2b: Boolean
    public abstract val baseURL: String
    public abstract val xParam: String
    public abstract val yParam: String
    public abstract val zParam: String

    /*
     * The number of tiles wide at each zoom level
     */
    private val mapWidthInTilesAtZoom: IntArray

    init {

        // init the num tiles wide
        var tileSize = this.getTileSize(0)

        mapWidthInTilesAtZoom = IntArray(totalMapZoom + 1)

        // for each zoom level
        for (z in totalMapZoom downTo 0) {
            mapWidthInTilesAtZoom[z] = tileSize / getTileSize(0)
            tileSize *= 2
        }
    }

    /**
     * @param zoom the zoom level
     * @return the map width in tiles
     */
    protected fun getMapWidthInTilesAtZoom(zoom: Int): Int = mapWidthInTilesAtZoom[zoom]

    /**
     * Returns the tile url for the specified tile at the specified zoom level. By default it will generate a tile url
     * using the base url and parameters specified in the constructor. Thus if <PRE><CODE>baseURl =
     * http://www.myserver.com/maps?version=0.1 xparam = x yparam = y zparam = z tilepoint = [1,2] zoom level = 3
    </CODE> *  </PRE> then the resulting url would be:
     * <pre>`http://www.myserver.com/maps?version=0.1&x=1&y=2&z=3`</pre> Note that the URL can be
     * a <CODE>file:</CODE> url.
     * @param zoom the zoom level
     * @param x the x value, measured from left to right
     * @param y the y value, measured from top to bottom
     * @return a valid url to load the tile
     */
    public open fun getTileUrl(x: Int, y: Int, zoom: Int): String {
        // System.out.println("getting tile at zoom: " + zoom);
        // System.out.println("map width at zoom = " + getMapWidthInTilesAtZoom(zoom));
        var yPart = "&$yParam=$y"

        // System.out.println("ypart = " + ypart);
        if (!yt2b) {
            val tileMax: Int = getMapWidthInTilesAtZoom(zoom)
            // int y = tilePoint.getY();
            yPart = "&$yParam=${(tileMax / 2 - y - 1)}"
        }

        return "$baseURL&$xParam=$x$yPart&$zParam=$zoom"
    }

    /**
     * Get the tile size.
     * @param zoom the zoom level
     * @return the tile size
     */
    protected open fun getTileSize(zoom: Int): Int = tileSize

    /**
     * Some map providers require explicit attribution, can be `null`
     * @return the attribution text
     */
    public open val attribution: String? = null

    /**
     * @return the license of the map provider, can be `null`
     */
    public open val license: String? = null
}

