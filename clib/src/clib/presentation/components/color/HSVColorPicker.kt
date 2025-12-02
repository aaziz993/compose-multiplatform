@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package clib.presentation.components.color

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import clib.presentation.components.color.common.ColorSlider
import clib.presentation.components.color.common.SelectedColorDetail
import com.github.skydoves.colorpicker.compose.ColorPickerController
import com.github.skydoves.colorpicker.compose.HsvColorPicker

/**
 * A composable function that creates a color picker UI for selecting HSV properties to get color. This component
 * contain one color spectrum, that consumer can drag and select their color. Using the other three sliders, consumer can change the
 * selected color's saturation, lightness and alpha values.
 * By adjusting these values, consumer can select or generate their desired color.
 *
 * @param controller [ColorPickerController].
 * @param value Color value.
 * @param onValueChange Callback on color value change.
 * @param modifier: The modifier to apply to this layout.
 * @param title: Title.
 *
 * @return @Composable: A color picker UI for selecting HSV colors.
 */
@Composable
internal fun HSVColorPicker(
    controller: ColorPickerController,
    value: Color,
    onValueChange: (Color) -> Unit,
    modifier: Modifier = Modifier,
    initialValue: Color? = null,
    title: String = "Select color hsv",
    brightness: String = "Brightness",
    alpha: String = "Alpha",
    hex: String = "Hex",
    copy: String = "Copy",
): Unit = Column(
    modifier = Modifier
        .shadow(
            elevation = 10.dp,
            shape = RoundedCornerShape(8.dp),
        )
        .padding(start = 12.dp, end = 12.dp, top = 12.dp, bottom = 12.dp)
        .then(modifier),
) {
    Text(
        text = title,
        textAlign = TextAlign.Start,
        modifier = Modifier
            .fillMaxWidth().padding(start = 12.dp, end = 12.dp, top = 12.dp),
        style = MaterialTheme.typography.bodySmall,
        fontSize = 12.sp,
    )

    HsvColorPicker(
        modifier = Modifier
            .fillMaxWidth()
            .weight(.6f),
        controller = controller,
        initialColor = initialValue,
        onColorChanged = { (color, _, fromUser) ->
            if (fromUser) onValueChange(color)
        },
    )

    Column(
        modifier = Modifier.weight(.4f),
    ) {
        ColorSlider(
            brightness,
            controller.selectedColor.value,
            100,
            controller.brightness.value,
        ) { value ->
            controller.setBrightness(value, true)
            onValueChange(controller.selectedColor.value)
        }

        ColorSlider(
            brightness,
            controller.selectedColor.value,
            100,
            controller.alpha.value,
        ) { value ->
            controller.setAlpha(value, true)
            onValueChange(controller.selectedColor.value)
        }
    }

    SelectedColorDetail(
        value,
        {
            onValueChange(it)
            controller.selectByColor(it, true)
        },
        Modifier,
        hex,
        copy,
    )
}
