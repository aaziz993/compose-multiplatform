package clib.presentation.components.color

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import clib.data.type.toColor
import clib.data.type.toColorMath
import clib.presentation.components.color.common.AlphaSlider
import clib.presentation.components.color.common.ColorSaturationAndLightnessSlider
import clib.presentation.components.color.common.SliderHue
import clib.presentation.components.color.common.selectColor
import com.github.ajalt.colormath.model.HSL
import com.github.skydoves.colorpicker.compose.ColorPickerController

/**
 * A composable function that creates a color picker UI for selecting HSL-A properties to get color. This component
 * contain one color spectrum, that consumer can drag and select their color. Using the other three sliders, consumer can change the
 * selected color's saturation, lightness and alpha values.
 * By adjusting these values, consumer can select or generate their desired color.
 *
 * @param controller: ColorPickerController.
 * @param modifier: The modifier to apply to this layout.
 * @param title: Title.
 *
 * @return @Composable: A color picker UI for selecting HSL-A colors.
 */
@Composable
internal fun HSLAColorPicker(
    controller: ColorPickerController,
    modifier: Modifier = Modifier,
    title: String = "Select color hsla",
    saturationLabel: String = "Saturation",
    lightnessLabel: String = "Lightness",
): Unit = Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
            // Sliders for adjusting HSL-A values.
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                var hsl by remember {
                    mutableStateOf(controller.selectedColor.value.toColorMath().toHSL())
                }

                if (hsl.h.isNaN()) hsl = hsl.copy(h = 0f)

                SliderHue(
                    modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
                    value = hsl.h,
                    onValueChange = { value ->
                        controller.selectColor(hsl.copy(h = value).toColor())
                        hsl = hsl.copy(h = value)
                    },
                )
                ColorSaturationAndLightnessSlider(
                    saturationLabel,
                    controller.selectedColor.value,
                    hsl.s,
                ) { value ->
                    controller.selectColor(hsl.copy(s = value).toColor())
                    hsl = hsl.copy(s = value)
                }
                ColorSaturationAndLightnessSlider(
                    lightnessLabel,
                    controller.selectedColor.value,
                    hsl.l,
                ) { value ->
                    controller.selectColor(hsl.copy(l = value).toColor())
                    hsl = hsl.copy(l = value)
                }
                AlphaSlider(controller)
            }
        }
    }
}
