package clib.presentation.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons

@Suppress("UnusedReceiverParameter")
public val Icons.Filled.Apple: ImageVector
    get() {
        if (_apple != null) return _apple!!
        _apple = Builder(
            name = "Apple",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                stroke = null,
                strokeLineWidth = 0f,
                strokeLineCap = Butt,
                strokeLineJoin = Miter,
                strokeLineMiter = 4f,
                pathFillType = NonZero,
            ) {
                moveTo(16.365f, 1.43f)
                curveToRelative(-1.19f, 0.017f, -2.603f, 0.823f, -3.45f, 1.84f)
                curveToRelative(-0.75f, 0.887f, -1.39f, 2.284f, -1.152f, 3.623f)
                curveToRelative(1.31f, 0.05f, 2.655f, -0.664f, 3.51f, -1.646f)
                curveToRelative(0.77f, -0.888f, 1.36f, -2.27f, 1.092f, -3.817f)
                close()
                moveTo(12f, 4.0f)
                curveToRelative(-1.63f, 0f, -3.23f, 1.01f, -4.04f, 2.58f)
                curveToRelative(-0.88f, 1.72f, -0.61f, 4.35f, 0.64f, 5.81f)
                curveToRelative(0.54f, 0.63f, 1.15f, 1.34f, 1.96f, 1.31f)
                curveToRelative(0.81f, -0.03f, 1.03f, -0.53f, 1.93f, -0.53f)
                curveToRelative(0.91f, 0f, 1.19f, 0.53f, 1.95f, 0.52f)
                curveToRelative(0.8f, -0.01f, 1.29f, -0.65f, 1.83f, -1.3f)
                curveToRelative(0.91f, -1.06f, 1.28f, -2.1f, 1.28f, -2.14f)
                curveToRelative(-0.03f, -0.01f, -2.53f, -0.97f, -2.56f, -3.83f)
                curveToRelative(-0.02f, -1.82f, 1.55f, -2.69f, 1.62f, -2.73f)
                curveToRelative(-0.73f, -1.06f, -1.88f, -1.21f, -2.3f, -1.23f)
                close()
            }
        }.build()
        return _apple!!
    }

private var _apple: ImageVector? = null
