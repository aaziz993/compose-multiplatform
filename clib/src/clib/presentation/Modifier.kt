package clib.presentation

import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp

/**
 * Adds a stroke to the bottom of a Composable.
 *
 * @param color The color of the stroke.
 * @param strokeWidth The width of the stroke.
 */
@Stable
public fun Modifier.bottomStroke(color: Color, strokeWidth: Dp): Modifier =
    then(
        drawWithContent {
            drawContent()
            drawLine(
                color = color,
                start = Offset(0f, size.height),
                end = Offset(size.width, size.height),
                strokeWidth = strokeWidth.toPx(),
            )
        },
    )

@Stable
public fun Modifier.fadingEdge(brush: Brush) =
    graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
        .drawWithContent {
            drawContent()
            drawRect(brush = brush, blendMode = BlendMode.DstIn)
        }
