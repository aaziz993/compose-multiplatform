package clib.presentation.components.map.model

import clib.data.location.toGeoPosition
import clib.presentation.components.map.tile.GoogleMaps
import clib.presentation.components.map.tile.OpenStreetMap
import clib.presentation.components.map.tile.VirtualEarth
import clib.presentation.components.map.tile.WMS
import java.awt.GridLayout
import java.awt.Rectangle
import java.awt.geom.Point2D
import java.io.File
import javax.swing.JComboBox
import javax.swing.JLabel
import javax.swing.JPanel
import klib.data.type.collections.updateSymmetric
import org.jxmapviewer.JXMapViewer
import org.jxmapviewer.OSMTileFactoryInfo
import org.jxmapviewer.VirtualEarthTileFactoryInfo
import org.jxmapviewer.WMSTileFactoryInfo
import org.jxmapviewer.cache.FileBasedLocalCache
import org.jxmapviewer.google.GoogleMapsTileFactoryInfo
import org.jxmapviewer.input.CenterMapListener
import org.jxmapviewer.input.PanKeyListener
import org.jxmapviewer.input.PanMouseInputListener
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor
import org.jxmapviewer.painter.CompoundPainter
import org.jxmapviewer.painter.Painter
import org.jxmapviewer.viewer.DefaultTileFactory
import org.jxmapviewer.viewer.GeoPosition
import org.jxmapviewer.viewer.TileFactory
import org.jxmapviewer.viewer.TileFactoryInfo

public class JxMapView(
    markers: Set<SwingWaypoint>? = null,
    routes: List<List<GeoPosition>>? = null,
    onSelect: ((Set<SwingWaypoint>, Set<SwingWaypoint>) -> Unit)? = null,
    view: MapView = MapView(),
) : JXMapViewer() {

    private val factories =
        view.tiles.map { tile ->
            when (tile) {
                is OpenStreetMap -> OSMTileFactoryInfo(tile.name, tile.baseURL)
                is VirtualEarth -> VirtualEarthTileFactoryInfo(
                    when (tile.mode) {
                        VirtualEarth.MAP -> VirtualEarthTileFactoryInfo.MAP
                        VirtualEarth.SATELLITE -> VirtualEarthTileFactoryInfo.SATELLITE
                        VirtualEarth.HYBRID -> VirtualEarthTileFactoryInfo.HYBRID
                    },
                )

                is GoogleMaps -> GoogleMapsTileFactoryInfo(tile.name, tile.baseURL, tile.key)

                is WMS -> WMSTileFactoryInfo(tile.minimumZoomLevel, tile.maximumZoomLevel, tile.totalMapZoom, tile.baseURL, tile.layers, tile.styles, tile.tileBgColor, tile.tileFormat, tile.srs, tile.tileSize)

                else -> TileFactoryInfo(
                    tile.name,
                    tile.minimumZoomLevel,
                    tile.maximumZoomLevel,
                    tile.totalMapZoom,
                    tile.tileSize,
                    tile.xr2l,
                    tile.yt2b,
                    tile.baseURL,
                    tile.xParam,
                    tile.yParam,
                    tile.zParam,
                )
            }.toCachedDefaultTileFactory()
        }

    private val selectedMarkers = mutableListOf<SwingWaypoint>()

    init {
        tileFactory = factories[0]

        view.camera.initialZoom?.let { zoom = it }
        view.camera.initialCenter?.let { addressLocation = it.toGeoPosition() }

        // Event listeners
        if (view.gestureOptions.isZoomEnabled) addMouseWheelListener(ZoomMouseWheelListenerCursor(this))

        if (view.gestureOptions.isMoveEnabled) {
            PanMouseInputListener(this).let {
                addMouseListener(it)
                addMouseMotionListener(it)
            }
            addMouseListener(CenterMapListener(this))
            addKeyListener(PanKeyListener(this))
        }

        overlayPainter = CompoundPainter(
            mutableListOf<Painter<JXMapViewer>>().apply {

                markers?.let { add(SwingWaypointPainter(this@JxMapView, it)) }

                routes?.forEach { add(RoutePainter(it)) }

                onSelect?.let { os ->
                    add(
                        SelectionPainter(
                            SelectionAdapter(this@JxMapView).also {
                                addMouseListener(it)
                                addMouseMotionListener(it)
                            },
                            markers?.let { ms ->
                                { rectangle ->
                                    val (left, right) = selectedMarkers.updateSymmetric(
                                        ms.filter {
                                            isSelected(it.position, rectangle)
                                        },
                                    )
                                    os(left, right)
                                }
                            },
                        ),
                    )
                }
            },
        )

        val tileLicenseLabel = JLabel(tileFactory.toLicenseText())

        if (view.isTilePickerEnabled)
            add(
                JPanel().apply {
                    layout = GridLayout()
                    add(JLabel(view.selectTile))
                    add(
                        JComboBox(factories.map { factory -> factory.info.name }.toTypedArray()).apply {
                            addItemListener {
                                tileFactory = factories[selectedIndex]
                                view.camera.initialZoom?.let { zoom = it }
                                tileLicenseLabel.setText(tileFactory.toLicenseText())
                            }
                        },
                    )
                },
            )

        if (view.ornamentOptions.isAttributionEnabled) add(tileLicenseLabel)
    }

    public fun toScreenPixel(geoPosition: GeoPosition): Point2D = tileFactory.geoToPixel(geoPosition, zoom).let {
        Point2D.Double(it.x - center.x + width / 2, it.y - center.y + height / 2)
    }

    public fun isSelected(geoPosition: GeoPosition, rectangle: Rectangle): Boolean = toScreenPixel(geoPosition).let {
        it.x.toInt() in rectangle.x until rectangle.x + rectangle.width && it.y.toInt() in rectangle.y until rectangle.y + rectangle.height
    }
}

private fun TileFactoryInfo.toCachedDefaultTileFactory() = DefaultTileFactory(this).apply {
    setLocalCache(
        FileBasedLocalCache(
            File(System.getProperty("user.home") + File.separator + ".$name" + ".jxmapviewer2"), false,
        ),
    )
    setThreadPoolSize(8)
}

private fun TileFactory.toLicenseText() = info.attribution + " - " + info.license
