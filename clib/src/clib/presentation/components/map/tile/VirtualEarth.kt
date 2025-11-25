package clib.presentation.components.map.tile

import kotlinx.serialization.Serializable

@Serializable
public data class VirtualEarth(
    override val name: String = "Virtual Earth",
    val mode: MVEMode = MAP,
) : Tile() {

    /**
     * The map mode
     */
    @Serializable
    public sealed class MVEMode(
        public val name: String,
        public val label: String,
        public val type: String,
        public val ext: String
    )

    /**
     * Use road map
     */
    public object MAP : MVEMode("map", "map", "r", ".png")

    /**
     * Use satellite map
     */
    public object SATELLITE : MVEMode("satellite", "satellite", "a", ".jpeg")

    /**
     * Use hybrid map
     */
    public object HYBRID : MVEMode("hybrid", "hybrid", "h", ".jpeg")

    override val minimumZoomLevel: Int
        get() = MIN_ZOOM_LEVEL
    override val maximumZoomLevel: Int
        get() = MAX_ZOOM_LEVEL
    override val totalMapZoom: Int
        get() = TOP_ZOOM_LEVEL
    override val tileSize: Int
        get() = TILE_SIZE
    override val xr2l: Boolean
        get() = false
    override val yt2b: Boolean
        get() = false
    override val baseURL: String
        get() = ""
    override val xParam: String
        get() = ""
    override val yParam: String
        get() = ""
    override val zParam: String
        get() = ""

    override fun getTileUrl(x: Int, y: Int, zoom: Int): String {
        val quad = tileToQuadKey(x, y, TOP_ZOOM_LEVEL - 0 - zoom)
        return "http://${mode.type}${quad[quad.length - 1]}.ortho.tiles.virtualearth.net/tiles/${mode.type}$quad${mode.ext}?g=1"
    }

    private fun tileToQuadKey(tx: Int, ty: Int, zl: Int): String {
        var quad = ""

        for (i in zl downTo 1) {
            val mask = 1 shl (i - 1)
            var cell = 0

            if ((tx and mask) != 0) cell++

            if ((ty and mask) != 0) cell += 2

            quad += cell
        }

        return quad
    }

    override val attribution: String
        get() = "\u00A9 Microsoft"

    override val license: String
        get() = "https://www.microsoft.com/en-us/maps/licensing"

    public companion object Companion {

        private const val TOP_ZOOM_LEVEL: Int = 19

        private const val MAX_ZOOM_LEVEL: Int = 17

        private const val MIN_ZOOM_LEVEL: Int = 2

        private const val TILE_SIZE: Int = 256
    }
}
