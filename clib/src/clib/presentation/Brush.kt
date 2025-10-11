package clib.presentation

import androidx.compose.animation.core.Easing
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

public fun Brush.Companion.easedGradient(
    easing: Easing,
    start: Offset = Offset.Zero,
    end: Offset = Offset.Infinite,
    numStops: Int = 16,
): Brush {
    val colors = List(numStops) { i ->
        val x = i * 1f / (numStops - 1)
        Color.Black.copy(alpha = 1f - easing.transform(x))
    }

    return linearGradient(colors = colors, start = start, end = end)
}

public fun Brush.Companion.easedVerticalGradient(
    easing: Easing,
    startY: Float = 0.0f,
    endY: Float = Float.POSITIVE_INFINITY,
    numStops: Int = 16,
): Brush = easedGradient(
    easing = easing,
    numStops = numStops,
    start = Offset(x = 0f, y = startY),
    end = Offset(x = 0f, y = endY),
)
