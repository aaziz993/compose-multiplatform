package clib.presentation.color

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import clib.data.type.color.toColor
import clib.presentation.color.common.AlphaSlider
import clib.presentation.color.common.ColorSaturationAndLightnessSlider
import clib.presentation.color.common.SliderHue

/**
 * A composable function that creates a color picker UI for selecting HSL-A properties to get color. This component
 * contain one color spectrum, that consumer can drag and select their color. Using the other three sliders, consumer can change the
 * selected color's saturation, lightness and alpha values.
 * By adjusting these values, consumer can select or generate their desired color.
 *
 * @param modifier: Modifier: The modifier to apply to this layout.
 * @param lastSelectedColor: Color: variable to pass last selected color.
 * @param onColorSelected: (selectedColor: Color) -> Unit: Callback to invoke when a color is selected.
 *
 * @return @Composable: A color picker UI for selecting HSL-A colors.
 */
@Composable
public fun HSLAColorPicker(
    modifier: Modifier = Modifier,
    title: String = "Select color hsla",
    lastSelectedColor: Color = Color.White,
    onColorSelected: (selectedColor: Color) -> Unit
) {
    val lastSelectedHsl = lastSelectedColor.toColor().toHSL()
    // State variables for HSL-A values
    val hue = rememberSaveable { mutableFloatStateOf(lastSelectedHsl.h) }
    val saturation = rememberSaveable { mutableFloatStateOf(lastSelectedHsl.s) }
    val lightness = rememberSaveable { mutableFloatStateOf(lastSelectedHsl.l) }
    val alpha = rememberSaveable { mutableFloatStateOf(lastSelectedColor.alpha) }

    // Derived state for the color based on HSL-A values
    val color by remember {
        derivedStateOf {
            Color.hsl(hue = hue.floatValue, saturation = saturation.floatValue, lightness = lightness.floatValue, alpha.floatValue)
        }
    }

    // Launch an effect to invoke the provided callback with the selected color
    LaunchedEffect(color) {
        onColorSelected.invoke(color)
    }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Column(
            modifier = Modifier
                .border(1.dp, Color.White, shape = RoundedCornerShape(8.dp))
                .shadow(
                    elevation = 10.dp,
                    shape = RoundedCornerShape(8.dp),
                )
                .background(Color.White)
                .padding(start = 12.dp, end = 12.dp, top = 12.dp, bottom = 12.dp),
        ) {
            Text(
                text = title,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth().padding(start = 12.dp, end = 12.dp, top = 12.dp),
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall,
                fontSize = 12.sp,
            )

            Row {
                // Sliders for adjusting HSL-A values
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                ) {
                    SliderHue(
                        Modifier.padding(top = 4.dp, bottom = 4.dp),
                        onColorSelect = { selectedColor ->
                            val selectedHsl = selectedColor.toColor().toHSL()
                            hue.floatValue = selectedHsl.h
                            saturation.floatValue = selectedHsl.s
                            lightness.floatValue = selectedHsl.l
                        },
                    )
                    ColorSaturationAndLightnessSlider("Saturation", saturation, color)
                    ColorSaturationAndLightnessSlider("Lightness", lightness, color)
                    AlphaSlider(alpha, color)
                }
            }
        }
    }
}
