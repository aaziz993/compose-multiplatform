package clib.presentation.components.color.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.CopyAll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
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
import clib.presentation.components.slider.ColorfulSlider
import clib.presentation.components.slider.MaterialSliderDefaults
import clib.presentation.components.slider.SliderBrushColor
import com.materialkolor.ktx.toHex
import klib.data.type.primitives.string.HEX_COLOR
import kotlinx.coroutines.launch

/**
 * A composable function that creates a slider for adjusting a float value associated with a color.
 *
 * @param label: String: The label to display alongside the slider.
 * @param trackColor: Color: The color used for the active track of the slider.
 * @param value: The value holding the current value of the slider.
 * @param onValueChange Callback for value change.
 *
 * @return @Composable: A slider UI for color selection.
 */
@Suppress("ComposeUnstableReceiver")
@Composable
internal fun ColumnScope.ColorSlider(
    label: String,
    trackColor: Color,
    maxValue: Int,
    value: Float,
    onValueChange: (Float) -> Unit
) {
    /**
     * Displays a slider for adjusting the given [value] associated with the provided [label].
     * The slider's active track color is set to [trackColor].
     */
    Row(
        modifier = Modifier.weight(1f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 14.sp,
            modifier = Modifier.weight(.2f),
        )

        val sliderText by remember(value) {
            derivedStateOf { toColorValue(value, maxValue).toString() }
        }

        ColorfulSlider(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(.8f),
            colors = MaterialSliderDefaults.defaultColors(
                thumbColor = SliderBrushColor(
                    color = MaterialTheme.colorScheme.primary,
                ),
                activeTrackColor = SliderBrushColor(
                    color = trackColor,
                ),
            ),
            borderStroke = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface),
        )

        Box(
            modifier = Modifier
                .width(80.dp)
                .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(20)),
        ) {
            BasicTextField(
                value = sliderText,
                onValueChange = {
                    val sanitized = sanitizeSliderValue(it, maxValue)
                    onValueChange(toSliderValue(sanitized, maxValue))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                textStyle = TextStyle(fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Center),
                maxLines = 1,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
        }
    }
}

/**
 * Converts a float value in the range [0, 1] to an integer color component in the range [0, maxValue].
 *
 * @return The integer representation of the color component.
 */
private fun toColorValue(value: Float, maxValue: Int): Int = (value * maxValue).toInt()

/**
 * Converts a string value in the range [0, maxValue] to a float value of slider in tha range of [0, 1]
 *
 * @return The float representation of the slider component
 */
private fun toSliderValue(value: String, maxValue: Int): Float = if (value.isEmpty()) 0f else value.toFloat() / maxValue

private fun sanitizeSliderValue(value: String, maxValue: Int): String =
    value.toFloatOrNull()?.let {
        when {
            it < 0 -> "0"
            it > maxValue -> maxValue.toString()
            else -> value
        }
    } ?: ""

/**
 * A composable function that displays the selected color and its details.
 *
 * @param value Color value.
 * @param onValueChange Callback on color value change.
 * @param title: Title.
 *
 * @return @Composable: A UI to display selected color and its details.
 */
@Composable
internal fun SelectedColorDetail(
    value: Color,
    onValueChange: (Color) -> Unit,
    @Suppress("ComposeModifierWithoutDefault") modifier: Modifier,
    title: String,
    copy: String,
) {
    var hex by remember { mutableStateOf(value.toHex()) }

    LaunchedEffect(value) {
        val newHex = value.toHex()
        if (hex != newHex) hex = newHex
    }

    Row {
        OutlinedTextField(
            value = hex,
            onValueChange = {
                hex = it
                if (Regex.HEX_COLOR.matches(it))
                    onValueChange(it.hexToColor())
            },
            modifier = Modifier
                .padding(2.dp)
                .weight(.8f),
            label = { Text(text = title) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Circle,
                    contentDescription = title,
                    tint = value,
                )
            },
            trailingIcon = {
                // Retrieve a ClipboardManager object.
                val clipboard = LocalClipboard.current
                val coroutineScope = rememberCoroutineScope()

                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            clipboard.setClipEntry(hex.toClipEntry())
                        }
                    },
                ) {
                    Icon(
                        imageVector = Icons.Filled.CopyAll,
                        contentDescription = copy,
                    )
                }
            },
            singleLine = true,
        )
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
    Column(Modifier.fillMaxHeight()) {
        colors.forEach { color ->
            val isSelected = value == color
            Box(
                modifier = Modifier
                    .size(boxSize)
                    .background(color, RectangleShape)
                    .clickable {
                        onValueChange(color)
                    }
                    .then(if (isSelected) Modifier.border(2.dp, Color.White) else Modifier),
            )
        }
    }
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
        Color900.getColorMap()[givenColor]!!.copy(alpha = alphaChange),
        Color800.getColorMap()[givenColor]!!.copy(alpha = alphaChange),
        Color700.getColorMap()[givenColor]!!.copy(alpha = alphaChange),
        Color600.getColorMap()[givenColor]!!.copy(alpha = alphaChange),
        Color.getColorMap()[givenColor]!!.copy(alpha = alphaChange),
        Color400.getColorMap()[givenColor]!!.copy(alpha = alphaChange),
        Color300.getColorMap()[givenColor]!!.copy(alpha = alphaChange),
        Color200.getColorMap()[givenColor]!!.copy(alpha = alphaChange),
        Color100.getColorMap()[givenColor]!!.copy(alpha = alphaChange),
        Color50.getColorMap()[givenColor]!!.copy(alpha = alphaChange),
    )
