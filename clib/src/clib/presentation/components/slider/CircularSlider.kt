package clib.presentation.components.slider

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import clib.data.type.LBlue
import klib.data.type.primitives.number.toDegrees
import klib.data.type.primitives.number.toRadians
import kotlin.math.*

/**
 * A Circular slider for Tracking progress. See https://github.com/Mindinventory/AndroidCircularSlider
 *
 * @param value to provide angle value for the slider.
 * @param onValueChanged to provide callback on angle value change for the slider.
 * @param radiusCircle radius of the circular slider.
 * @param progressWidth width of the Progress.
 * @param strokeCap to set strokes of the ends.
 * @param thumbRadius to set the radius of the thumb.
 * @param tickColor to set the color of the minute-like clock arms.
 * @param tickHighlightedColor to set the color of the hour-like clock arms.
 * @param dialColor  dial color.
 * @param progressColor color of the progress bar.
 * @param startThumbCircleColor Initial thumb color.
 * @param thumbColor Thumb color.
 * @param trackColor track color.
 * @param trackWidth width of the track.
 * @param animate Flag to set enabled/disabled animation on circular slider.
 * @param animationSpec to set the sliding animation.
 * @param content Content to put inside circular slider.
 */
@Suppress("ComposeParameterOrder", "ComposeModifierMissing")
@Composable
public fun CircularSlider(
    value: Double = 0.0,
    onValueChanged: (Double) -> Unit,
    radiusCircle: Dp = 150.dp,
    progressWidth: Float = 28f,
    strokeCap: StrokeCap = StrokeCap.Round,
    thumbRadius: Float = 20f,
    tickColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
    tickHighlightedColor: Color = MaterialTheme.colorScheme.primary,
    dialColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    progressColor: Brush = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.primaryContainer,
        ),
    ),
    startThumbCircleColor: List<Color>? = null,
    thumbColor: List<Color> = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.primaryContainer,
    ),
    thumbShadowColor: Color? = Color.LBlue.copy(alpha = 0.20f),
    trackColor: List<Color> = listOf(
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
    ),
    circleColor: Color = Color.Transparent,
    trackWidth: Float = thumbRadius,
    animate: Boolean = false,
    animationSpec: AnimationSpec<Float> = tween(1000),
    onTouchEnabled: Boolean = true,
    onDragEnabled: Boolean = true,
    content: @Composable () -> Unit = {
        Text(
            text = "${(value * 100 / 360).roundToInt()} %",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.rotate(90f),
        )
    },
) {
    // Center of the shape.
    var shapeCenter by remember {
        mutableStateOf(Offset.Zero)
    }

    val animatedAngle by animateFloatAsState(
        targetValue = value.toFloat(),
        animationSpec = animationSpec,
    )

    // Progressbar.
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(radiusCircle * 2f)
            .rotate(-90f),
    ) {
        Canvas(
            modifier = Modifier
                .size(radiusCircle * 2f)
                .pointerInput(onDragEnabled, onTouchEnabled) {
                    if (onDragEnabled)
                        detectDragGestures { change, _ ->
                            onValueChanged(getRotationAngle(change.position, shapeCenter))
                            change.consume()
                        }

                    if (onTouchEnabled)
                        detectTapGestures { offset ->
                            onValueChanged(getRotationAngle(offset, shapeCenter))
                        }
                },
        ) {
            shapeCenter = center
            val radius = size.minDimension / 2

            val x = (shapeCenter.x + cos(value.toRadians()) * radius).toFloat()
            val y = (shapeCenter.y + sin(value.toRadians()) * radius).toFloat()
            val handleCenter = Offset(x, y)

            // track.
            drawCircle(
                brush = Brush.sweepGradient(
                    colors = trackColor,
                    center = center,
                ),
                style = Stroke(trackWidth),
                radius = radius,
            )

            // Progress.
            drawArc(
                brush = progressColor,
                startAngle = 0f,
                sweepAngle = if (animate) animatedAngle else value.toFloat(),
                useCenter = false,
                style = Stroke(progressWidth),
            )

            // Initial thumb circle at (0,0) coordinate.
            startThumbCircleColor?.let {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = it.onEach { color -> color.copy(alpha = 0.10f) },
                        center = Offset(size.width, size.height / 2),
                    ),
                    center = Offset(size.width, size.height / 2),
                    radius = thumbRadius + 5f,
                )
            }

            //Thumb
            drawCircle(
                brush = Brush.radialGradient(colors = thumbColor, center = handleCenter),
                center = handleCenter,
                radius = thumbRadius,
            )
            // Inner Thumb.
            startThumbCircleColor?.let {
                drawCircle(
                    color = it[0],
                    center = handleCenter,
                    radius = thumbRadius - 10f,
                )
            }
            // Outer Shadow.
            thumbShadowColor?.let {
                drawCircle(
                    color = it,
                    center = handleCenter,
                    radius = thumbRadius + 20f,
                )
            }

            // Extra layer to increase touch area.
            drawCircle(
                color = Color.Transparent,
                center = handleCenter,
                radius = thumbRadius + 40f,
            )
        }
        /**
         * Extra canvas layer to intercept touch event.
         */
        Canvas(
            modifier = Modifier
                .clip(CircleShape)
                .size((radiusCircle) * 1.5f)
                .clickable(
                    enabled = false,
                    onClick = {},
                ),
        ) {
            drawCircle(
                color = Color.Transparent,
                radius = (radiusCircle - 16.dp).toPx(),
                blendMode = BlendMode.Screen,
            )
        }

        content()
    }
}

private sealed class ClockLineType {
    object Minutes : ClockLineType()
    object Hours : ClockLineType()
}

private fun getRotationAngle(currentPosition: Offset, center: Offset): Double {
    val (dx, dy) = currentPosition - center
    val theta = atan2(dy, dx).toDouble()

    var angle = theta.toDegrees()

    if (angle < 0) angle += 360.0

    if (angle >= 359) angle = 0.0

    return angle
}
