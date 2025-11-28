package clib.presentation.components.color.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CopyAll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import clib.data.toClipEntry
import clib.data.type.Color100
import clib.data.type.Color200
import clib.data.type.Color300
import clib.data.type.Color400
import clib.data.type.Color50
import clib.data.type.Color600
import clib.data.type.Color700
import clib.data.type.Color800
import clib.data.type.Color900
import clib.data.type.getColorMap
import clib.data.type.hexToColor
import com.github.skydoves.colorpicker.compose.ColorPickerController
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.materialkolor.ktx.toHex
import klib.data.type.primitives.number.decimal.formatter.DecimalFormatter
import klib.data.type.primitives.string.HEX_COLOR
import kotlin.math.roundToInt
import kotlinx.coroutines.launch

@Composable
public fun rememberColorPickerController(initialValue: Color): ColorPickerController =
    rememberColorPickerController().apply {
        selectColor(initialValue)
    }

internal fun ColorPickerController.selectColor(color: Color) {
    (selectedColor as MutableState<Color>).value = color
}

/**
 * A composable function that creates a slider for adjusting a float value associated with a color.
 *
 * @param label: String: The label to display alongside the slider.
 * @param trackColor: Color: The color used for the active track of the slider.
 * @param value: MutableState<Float>: The mutable state holding the current color value of the slider.
 * @param onValueChange: (value: Float) -> Unit: Callback to invoke when the slider value changes.: The mutable state holding the current color value of the slider.
 *
 * @return @Composable: A slider UI for color selection.
 */
@Composable
internal fun ColorSlider(
    label: String,
    trackColor: Color,
    value: Float,
    onValueChange: (Float) -> Unit
) {
    var sliderValue by remember { mutableStateOf(TextFieldValue((value * 255).toInt().toString())) }

    /**
     * Displays a slider for adjusting the given [value] associated with the provided [label].
     * The slider's active track color is set to [trackColor].
     */
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            text = label,
            color = Color.Black,
            fontSize = 14.sp,
            modifier = Modifier.weight(.2f),
        )
        Slider(
            value = value,
            onValueChange = { value ->
                onValueChange(value)
                sliderValue = TextFieldValue(toColorRange(value).toString())
            },
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = trackColor,
            ),
            modifier = Modifier.weight(.8f),
        )
        Box(
            modifier = Modifier
                .width(80.dp)
                .weight(.2f)
                .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(20)),
        ) {
            BasicTextField(
                value = sliderValue,
                onValueChange = {
                    sliderValue = if (it.text != "" && it.text.toInt() > 255) TextFieldValue("255") else it
                    onValueChange(sliderRange(sliderValue.text))
                },
                modifier = Modifier
                    .width(80.dp)
                    .padding(5.dp),
                textStyle = TextStyle(fontSize = 12.sp, color = Color.Black, textAlign = TextAlign.Center),
                maxLines = 1,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
        }

    }
}

/**
 * Converts a float value in the range [0, 1] to an integer color component in the range [0, 255].
 *
 * @return The integer representation of the color component.
 */
private fun toColorRange(value: Float): Int = (value * 255 + 0.5f).toInt()

/**
 * Converts a string value in the range [0, 255] to a float value of slider in tha range of [0, 1]
 *
 * @return The float representation of the slider component
 */
private fun sliderRange(value: String): Float = if (value.isEmpty()) 0f else value.toFloat() / 255

/**
 * A composable function that creates a slider for adjusting a float value associated with a color alpha valuw.
 *
 * @param controller: ColorPickerController.
 *
 * @return @Composable: A slider UI for alpha selection.
 */
@Composable
internal fun AlphaSlider(controller: ColorPickerController) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            text = "Alpha",
            color = Color.Black,
            fontSize = 12.sp,
            modifier = Modifier.weight(.2f),
        )
        Slider(
            value = controller.selectedColor.value.alpha,
            onValueChange = { value ->
                controller.setAlpha(value, true)
            },
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = controller.selectedColor.value,
            ),
            valueRange = 0f..1f,
            modifier = Modifier.weight(.8f),
        )

        val displayText = DecimalFormatter.DefaultFormatter.format(
            (controller.selectedColor.value.alpha * 10000).roundToInt(),
        ).displayValue

        Text(
            text = displayText,
            modifier = Modifier
                .width(25.dp)
                .weight(.2f),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Black,
        )
    }
}

/**
 * A composable function that creates a slider for adjusting a float value associated with a color alpha value.
 *
 * @param label: String: The label to display alongside the slider.
 * @param trackColor: Color: The color used for the active track of the slider.
 * @param value: The value holding the current value of the slider.
 * @param onValueChange Callback for value change.
 *
 * @return @Composable: A slider UI for alpha selection.
 */
@Composable
internal fun ColorSaturationAndLightnessSlider(
    label: String,
    trackColor: Color,
    value: Float,
    onValueChange: (Float) -> Unit,
) {
    /**
     * The slider's active track color is set to [trackColor].
     * Displays a slider for adjusting the given [value] associated with the provided [label].
     */
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            text = label,
            color = Color.Black,
            fontSize = 12.sp,
            modifier = Modifier.weight(.2f),
        )
        Slider(
            value = value,
            onValueChange = onValueChange,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = trackColor,
            ),
            valueRange = 0f..1f,
            modifier = Modifier.weight(.8f),
        )

        val displayText = DecimalFormatter.DefaultFormatter.format((value * 10000).roundToInt()).displayValue

        Text(
            text = displayText,
            modifier = Modifier
                .width(25.dp)
                .weight(.1f),
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Black,
        )
    }
}

/**
 * A composable function that displays the selected color and its details.
 *
 * @param controller: ColorPickerController.
 * @param title: Title.
 *
 * @return @Composable: A UI to display selected color and its details.
 */
@Composable
internal fun SelectedColorDetail(
    controller: ColorPickerController,
    title: String,
) {
    // Retrieve a ClipboardManager object
    val clipboard = LocalClipboard.current

    Row(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
    ) {
        // Display the current color in a Box with a MaterialTheme shape
        Column(
            verticalArrangement = Arrangement.Center,
        ) {
            Box(
                modifier = Modifier
                    .padding(start = 12.dp, end = 12.dp, top = 22.dp, bottom = 12.dp)
                    .height(75.dp)
                    .width(75.dp)
                    .background(controller.selectedColor.value, shape = MaterialTheme.shapes.large)
                    .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp)),
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            Text(
                text = title,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.bodyMedium,
            )

            val hex = controller.selectedColor.value.toHex()

            Row {
                OutlinedTextField(
                    modifier = Modifier
                        .padding(2.dp)
                        .weight(.8f),
                    value = hex,
                    maxLines = 1,
                    label = { Text(text = title) },
                    onValueChange = { value ->
                        if (Regex.HEX_COLOR.matches(value))
                            controller.selectColor(value.hexToColor())
                    },
                )

                val coroutineScope = rememberCoroutineScope()

                Icon(
                    imageVector = Icons.Filled.CopyAll,
                    contentDescription = "copy icon",
                    modifier = Modifier
                        .width(45.dp)
                        .height(45.dp)
                        .padding(8.dp)
                        .clickable {
                            coroutineScope.launch {
                                clipboard.setClipEntry(hex.toClipEntry())
                            }
                        },
                )
            }
        }
    }
}

/**
 * A composable function that displays a color colum with predefined colors.
 *
 * @param givenColor: KvColor: The color to generate color palette. This is from [Color]
 * @param value: Color: The selected color to highlight.
 * @param onValueChange: (color: Color) -> Unit: Callback to invoke when a color is selected.
 */
@Composable
internal fun ColorColumn(
    boxSize: Dp,
    givenColor: String,
    value: Color,
    onValueChange: (color: Color) -> Unit
) {
    val colors = generateColorPalette(givenColor = givenColor)
    Column {
        colors.forEach {
            ColorBox(
                boxColor = it,
                value = value,
                onValueChange = onValueChange,
                boxSize = boxSize,
            )
        }
    }
}

/**
 * A composable function that displays a single color box.
 *
 * @param boxSize: Dp: Size of the color box.
 * @param boxColor: Color: The color to display.
 * @param value: Color: The selected color to highlight.
 * @param onValueChange: (color: Color) -> Unit: Callback to invoke when a color is selected.
 */
@Composable
internal fun ColorBox(
    boxSize: Dp,
    boxColor: Color,
    value: Color?,
    onValueChange: (color: Color) -> Unit,
) {
    var isSelected by remember { mutableStateOf(false) }

    value?.let {
        isSelected = boxColor == it
    }

    Box(
        modifier = Modifier
            .width(boxSize)
            .height(boxSize)
            .background(boxColor, RectangleShape)
            .clickable {
                isSelected = true
                onValueChange(boxColor)
            }
            .then(if (isSelected) Modifier.border(2.dp, Color.White) else Modifier),
    )
}

/**
 * Generate a list of colors with pre-defined color packages. According to the feeding color,
 * this method generate a list of colors.
 *
 * @param givenColor The color to generate the color packages for.
 * @return A list of colors.
 */
private fun generateColorPalette(givenColor: String, alphaChange: Float = 1f): List<Color> =
    listOf(
        Color900.getColorMap().entries.single { (key, _) -> key == givenColor }.value.copy(alpha = alphaChange),
        Color800.getColorMap().entries.single { (key, _) -> key == givenColor }.value.copy(alpha = alphaChange),
        Color700.getColorMap().entries.single { (key, _) -> key == givenColor }.value.copy(alpha = alphaChange),
        Color600.getColorMap().entries.single { (key, _) -> key == givenColor }.value.copy(alpha = alphaChange),
        Color.getColorMap().entries.single { (key, _) -> key == givenColor }.value.copy(alpha = alphaChange),
        Color400.getColorMap().entries.single { (key, _) -> key == givenColor }.value.copy(alpha = alphaChange),
        Color300.getColorMap().entries.single { (key, _) -> key == givenColor }.value.copy(alpha = alphaChange),
        Color200.getColorMap().entries.single { (key, _) -> key == givenColor }.value.copy(alpha = alphaChange),
        Color100.getColorMap().entries.single { (key, _) -> key == givenColor }.value.copy(alpha = alphaChange),
        Color50.getColorMap().entries.single { (key, _) -> key == givenColor }.value.copy(alpha = alphaChange),
    )
