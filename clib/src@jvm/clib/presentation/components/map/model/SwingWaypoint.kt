package clib.presentation.components.map.model

import clib.data.location.toLocation
import java.awt.Dimension
import java.awt.Image
import java.io.ByteArrayInputStream
import javax.imageio.ImageIO
import javax.swing.ImageIcon
import javax.swing.JButton
import javax.swing.JEditorPane
import javax.swing.JOptionPane
import javax.swing.event.HyperlinkEvent
import klib.data.location.Location
import org.jxmapviewer.viewer.DefaultWaypoint
import org.jxmapviewer.viewer.GeoPosition

public class SwingWaypoint(
    geoPosition: GeoPosition,
    public val id: String? = null,
    public val description: String? = null,
    size: Dimension = Dimension(30, 40),
    public val offset: Dimension = Dimension(-15, -40),
    image: ByteArray = Marker.image,
    onHyperLinkClick: ((Location, href: String) -> Boolean)? = null
) : DefaultWaypoint(geoPosition) {

    public val button: JButton = JButton().apply {
        this.size = size

        preferredSize = size

        setFocusable(false)

        setOpaque(false)

        setContentAreaFilled(false)

        setBorderPainted(false)

        setIcon(
            ImageIcon(
                ImageIcon(
                    ImageIO.read(ByteArrayInputStream(image)),
                ).image.getScaledInstance(size.width, size.height, Image.SCALE_SMOOTH),
            ),
        )

        if (description == null) {
            isEnabled = false
        }
        else {
            addActionListener {
                val message = JEditorPane(
                    "text/html",
                    description,
                ).apply {
                    isEditable = false
                }

                val dialog = JOptionPane(message, JOptionPane.PLAIN_MESSAGE).createDialog(this, id)

                message.addHyperlinkListener {
                    if (it.eventType == HyperlinkEvent.EventType.ACTIVATED) {
                        if (onHyperLinkClick?.invoke(toLocation(), it.url.toString()) == true) {
                            dialog.dispose()
                        }
                    }
                }

                dialog.isVisible = true
            }
        }
        isVisible = true
    }
}
