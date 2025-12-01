package clib.presentation.components.color.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Suppress("ComposeParameterOrder")
@Composable
internal fun SliderHue(
    modifier: Modifier = Modifier,
    value: Float,
    onValueChange: (value: Float) -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        var huePanelWidth: Float? by remember { mutableStateOf(null) }

        HuePanel { width -> huePanelWidth = width }

        huePanelWidth?.let { width ->
            val hue = hueToPoint(value, width)

            Slider(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                valueRange = 0f..width,
                value = hue,
                onValueChange = {
                    onValueChange(pointToHue(it, width))
                },
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.primary,
                    activeTrackColor = Color.Transparent,
                    inactiveTrackColor = Color.Transparent,
                ),
                thumb = {
                    Box(
                        modifier = Modifier
                            .height(45.dp)
                            .width(10.dp)
                            .background(Color.Transparent, shape = MaterialTheme.shapes.large)
                            .border(
                                2.dp,
                                Color.Gray,
                                RoundedCornerShape(12.dp),
                            ),
                    )
                },
            )
        }
    }
}

@Composable
private fun HuePanel(onDrown: (width: Float) -> Unit) =
    Canvas(
        modifier = Modifier
            .padding(top = 1.dp)
            .height(45.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12)),
    ) {
        drawRect(
            brush = Brush.horizontalGradient(
                colors = List(361) { hue ->
                    Color.hsv(hue.toFloat(), 1f, 1f)
                },
            ),
            size = size,
        )

        onDrown(size.width)
    }

private fun pointToHue(pointX: Float, huePanelWidth: Float): Float = pointX * 360f / huePanelWidth

private fun hueToPoint(value: Float, huePanelWidth: Float): Float = value * huePanelWidth / 360f

