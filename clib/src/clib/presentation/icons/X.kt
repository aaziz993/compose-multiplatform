package clib.presentation.icons

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

@Suppress("UnusedReceiverParameter")
public val Icons.Filled.X: ImageVector
    get() {
        if (_X != null) {
            return _X!!
        }
        _X = ImageVector.Builder(
            name = "X",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(13.795f, 10.533f)
                lineTo(20.68f, 2f)
                horizontalLineToRelative(-3.073f)
                lineToRelative(-5.255f, 6.517f)
                lineTo(7.69f, 2f)
                lineTo(1f, 2f)
                lineToRelative(7.806f, 10.91f)
                lineTo(1.47f, 22f)
                horizontalLineToRelative(3.074f)
                lineToRelative(5.705f, -7.07f)
                lineTo(15.31f, 22f)
                lineTo(22f, 22f)
                lineToRelative(-8.205f, -11.467f)
                close()
                moveTo(11.415f, 13.483f)
                lineTo(9.97f, 11.464f)
                lineTo(4.36f, 3.627f)
                horizontalLineToRelative(2.31f)
                lineToRelative(4.528f, 6.317f)
                lineToRelative(1.443f, 2.02f)
                lineToRelative(6.018f, 8.409f)
                horizontalLineToRelative(-2.31f)
                lineToRelative(-4.934f, -6.89f)
                close()
            }
        }.build()

        return _X!!
    }

@Suppress("ObjectPropertyName")
private var _X: ImageVector? = null
