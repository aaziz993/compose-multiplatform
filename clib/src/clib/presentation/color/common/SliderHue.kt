package clib.presentation.color.common

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import clib.data.type.color.toColor
import com.github.ajalt.colormath.model.HSV

@Composable
internal fun SliderHue(
    modifier: Modifier = Modifier,
    onColorSelect: (color: Color) -> Unit
) {
    val hueValueState = rememberSaveable { mutableFloatStateOf(0f) }
    val huePanelWidth = rememberSaveable { mutableFloatStateOf(1f) }

    var hsv by remember { mutableStateOf(Color.Blue.toColor().toHSV()) }

    // Launch an effect to invoke the provided callback with the selected color
    LaunchedEffect(hueValueState.floatValue) {
        val selectedHue = pointToHue(hueValueState.floatValue, huePanelWidth.value)
        hsv = HSV(selectedHue, hsv.s, hsv.v)
        val generatedColor = Color.hsv(hsv.h, hsv.s, hsv.v)
        onColorSelect.invoke(generatedColor)
    }

    Box(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        HuePanel(huePanelWidth)

        Slider(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            valueRange = 0f..huePanelWidth.value,
            value = hueValueState.floatValue,
            onValueChange = hueValueState.component2(),
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

@Composable
private fun HuePanel(huePanelWidth: MutableState<Float>) {
    Canvas(
        modifier = Modifier
            .padding(top = 1.dp)
            .height(45.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12)),
    ) {
        huePanelWidth.value = size.width

        drawRect(
            brush = Brush.horizontalGradient(
                colors = List(361) { hue ->
                    Color.hsv(hue.toFloat(), 1f, 1f)
                },
            ),
            size = size,
        )
    }
}

private fun pointToHue(pointX: Float, huePanelWidth: Float): Float = pointX * 360f / huePanelWidth
