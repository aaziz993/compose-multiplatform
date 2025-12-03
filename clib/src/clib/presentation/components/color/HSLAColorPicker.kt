package clib.presentation.components.color

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import clib.data.type.toColor
import clib.data.type.toHSL
import clib.presentation.components.color.common.ColorSlider
import clib.presentation.components.color.common.SelectedColorDetail
import clib.presentation.components.slider.CircularSlider
import klib.data.type.primitives.number.decimal.formatter.DecimalFormatter
import kotlin.math.roundToInt

/**
 * A composable function that creates a color picker UI for selecting HSL-A properties to get color. This component
 * contain one color spectrum, that consumer can drag and select their color. Using the other three sliders, consumer can change the
 * selected color's saturation, lightness and alpha values.
 * By adjusting these values, consumer can select or generate their desired color.
 *
 * @param value Color value.
 * @param onValueChange Callback on color value change.
 * @param modifier: The modifier to apply to this layout.
 * @param title: Title.
 *
 * @return @Composable: A color picker UI for selecting HSL-A colors.
 */
@Composable
internal fun HSLAColorPicker(
    value: Color,
    onValueChange: (Color) -> Unit,
    modifier: Modifier = Modifier,
    title: String = "Select color hsla",
    saturation: String = "Saturation",
    lightness: String = "Lightness",
    alpha: String = "Alpha",
    hex: String = "Hex",
    copy: String = "Copy",
): Unit = Column(
    modifier = modifier,
) {
    Text(
        text = title,
        textAlign = TextAlign.Start,
        modifier = Modifier
            .fillMaxWidth().padding(start = 12.dp, end = 12.dp, top = 12.dp),
        style = MaterialTheme.typography.bodySmall,
        fontSize = 12.sp,
    )

    var hsl by remember {
        mutableStateOf(value.toColor().toHSL().let { if (it.h.isNaN()) it.copy(h = 0f) else it })
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .weight(.5f),
        contentAlignment = Alignment.Center,
    ) {
        // Take the smaller of width/height as diameter.
        val diameter = min(maxWidth, maxHeight)
        val radiusCircle = diameter / 2f

        CircularSlider(
            value = hsl.h.toDouble(),
            onValueChanged = {
                hsl = hsl.copy(h = it.toFloat())
                onValueChange(hsl.toColor())
            },
            radiusCircle = radiusCircle,
            progressColor = Brush.linearGradient(
                colors = listOf(Color.Transparent.copy(alpha = .2f), Color.Transparent.copy(alpha = .2f)),
            ),
            trackColor = List(361) { hue ->
                Color.hsv(hue.toFloat(), 1f, 1f)
            },
            animate = true,
        ) {
            val displayText = DecimalFormatter.DefaultFormatter.format((hsl.h * 100).roundToInt().toString()).displayValue
            Text(
                text = displayText,
                color = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.rotate(90f),
            )
        }
    }

    Column(
        modifier = Modifier.weight(.3f),
    ) {
        ColorSlider(
            saturation,
            value,
            100,
            hsl.s,
        ) {
            hsl = hsl.copy(s = it)
            onValueChange(hsl.toColor())
        }
        ColorSlider(
            lightness,
            value,
            100,
            hsl.l,
        ) {
            hsl = hsl.copy(l = it)
            onValueChange(hsl.toColor())
        }
        ColorSlider(
            alpha,
            value,
            100,
            hsl.alpha,
        ) {
            hsl = hsl.copy(alpha = it)
            onValueChange(hsl.toColor())
        }
    }

    SelectedColorDetail(
        value,
        {
            onValueChange(it)
            hsl = it.toHSL()
        },
        Modifier.weight(.2f),
        hex,
        copy,
    )
}
