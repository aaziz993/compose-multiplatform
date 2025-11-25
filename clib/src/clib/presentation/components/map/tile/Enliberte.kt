package clib.presentation.components.map.tile

import kotlinx.serialization.Serializable

@Serializable
public class Enliberte(
    override val name: String = "Enliberte",
    override val baseURL: String = BRIGHT,
) : Tile() {

    override val minimumZoomLevel: Int
        get() = 0
    override val maximumZoomLevel: Int
        get() = MAX_ZOOM
    override val totalMapZoom: Int
        get() = MAX_ZOOM
    override val tileSize: Int
        get() = 256
    override val xr2l: Boolean
        get() = false
    override val yt2b: Boolean
        get() = true
    override val xParam: String
        get() = "x"
    override val yParam: String
        get() = "y"
    override val zParam: String
        get() = "z"

    override fun getTileUrl(x: Int, y: Int, zoom: Int): String = "$baseURL/$zoom/$x/$y.png"

    override val attribution: String
        get() = "© OpenStreetMap contributors, Humanitarian OpenStreetMap Team"

    override val license: String
        get() = "Creative Commons Attribution-ShareAlike 2.0"

    public companion object {

        private const val MAX_ZOOM = 19

        public const val BRIGHT: String = "https://tuiles.enliberte.fr/styles/bright.json"
        public const val BASIC: String = "https://tuiles.enliberte.fr/styles/basic.json"
        public const val MAP_TILER_3D: String = "https://tuiles.enliberte.fr/styles/maptiler-3d.json"
        public const val POSITRON: String = "https://tuiles.enliberte.fr/styles/positron.json"
        public const val LIBERTY: String = "https://tuiles.enliberte.fr/styles/liberty.json"
        public const val FIORD: String = "https://tuiles.enliberte.fr/styles/fiord.json"
        public const val DARK: String = "https://tuiles.enliberte.fr/styles/dark.json"
        public const val TONER: String = "https://tuiles.enliberte.fr/styles/toner.json"
    }
}
