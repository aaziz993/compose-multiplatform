package clib.presentation.color

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import clib.data.type.hexToColor
import clib.data.type.toHex
import clib.presentation.color.model.ColorPicker
import clib.presentation.color.model.ColorView
import com.github.skydoves.colorpicker.compose.AlphaSlider
import com.github.skydoves.colorpicker.compose.AlphaTile
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorPickerController
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController

@Composable
public fun ColorPickerDialog(
    onDismissRequest: () -> Unit,
    initialColor: Color,
    onConfirm: (Color) -> Unit,
    picker: ColorPicker = ColorPicker(),
    view: ColorView = ColorView(),
) {
    val controller = rememberColorPickerController()
    controller.debounceDuration = picker.debounceDuration
    val recentColors = remember { mutableStateListOf<Color>() }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        shape = picker.dialogShape,
        title = { Text(picker.title) },
        text = {
            Column(
                modifier = Modifier.padding(picker.dialogPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // HSV wheel.
                if (picker.useHsvWheel)
                    HsvColorPicker(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(450.dp)
                            .padding(10.dp),
                        controller = controller,
                    )

                // Alpha slider.
                if (picker.useAlphaSlider) {
                    Spacer(Modifier.height(12.dp))
                    AlphaSlider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .height(35.dp),
                        controller = controller,
                    )
                }

                // Brightness slider.
                if (picker.useAlphaSlider) {
                    Spacer(Modifier.height(12.dp))
                    BrightnessSlider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .height(35.dp),
                        controller = controller,
                    )
                }

                // RGB input.
                if (picker.useRgbFields) {
                    Spacer(Modifier.height(12.dp))
                    RgbInputFields(
                        controller,
                        picker.redLabel,
                        picker.greenLabel,
                        picker.blueLabel,
                    )
                }

                // HEX input.
                if (picker.useHexField) {
                    Spacer(Modifier.height(12.dp))
                    HexColorInput(controller, picker.hexLabel)
                }

                // Alpha tile.
                if (picker.useAlphaTile) {
                    Spacer(Modifier.height(12.dp))
                    AlphaTile(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(6.dp)),
                        controller = controller,
                    )
                }

                // Recent colors.
                if (picker.showRecentColors && recentColors.isNotEmpty()) {
                    Spacer(Modifier.height(12.dp))
                    RecentColors(
                        controller,
                        picker.recentColorsLabel,
                        recentColors,
                        picker.recentColorsLimit,
                    )
                }

                // Initial color sync
                LaunchedEffect(initialColor) {
                    controller.selectByColor(initialColor, fromUser = false)
                }
            }
        },
        confirmButton = {
            IconButton(
                onClick = {
                    val selected = controller.selectedColor.value

                    // Add to recent colors
                    if (picker.showRecentColors && selected !in recentColors) {
                        recentColors.add(0, selected)
                        if (recentColors.size > picker.recentColorsLimit) recentColors.removeLast()
                    }

                    onConfirm(selected)
                },
            ) {
                Text(picker.confirmText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(picker.cancelText)
            }
        },
    )
}

@Composable
private fun HexColorInput(
    controller: ColorPickerController,
    hexLabel: String
) {
    val selected = controller.selectedColor.value

    var hex by remember(selected) { mutableStateOf(selected.toHex()) }

    OutlinedTextField(
        value = hex,
        onValueChange = { value ->
            hex = value.uppercase()
            val parsed = runCatching { hex.hexToColor() }.getOrNull()
            if (parsed != null) controller.selectByColor(parsed, fromUser = true)
        },
        label = { Text(hexLabel) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun RgbInputFields(
    controller: ColorPickerController,
    redLabel: String,
    greenLabel: String,
    blueLabel: String,
) {
    val color = controller.selectedColor.value

    var red by remember(color) { mutableStateOf((color.red * 255).toInt()) }
    var green by remember(color) { mutableStateOf((color.green * 255).toInt()) }
    var blue by remember(color) { mutableStateOf((color.blue * 255).toInt()) }

    fun update() {
        controller.selectByColor(Color(red, green, blue), fromUser = true)
    }

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        RgbField(redLabel, red) { value -> red = value; update() }
        RgbField(greenLabel, green) { value -> green = value; update() }
        RgbField(blueLabel, blue) { value -> blue = value; update() }
    }
}

@Composable
private fun RgbField(
    label: String,
    value: Int,
    onValue: (Int) -> Unit
) {
    OutlinedTextField(
        value = value.toString(),
        onValueChange = { txt ->
            txt.toIntOrNull()?.let {
                onValue(it.coerceIn(0, 255))
            }
        },
        label = { Text(label) },
        singleLine = true,
        modifier = Modifier.width(80.dp),
    )
}

@Composable
private fun RecentColors(
    controller: ColorPickerController,
    recentLabel: String,
    recentColors: List<Color>,
    recentColorsLimit: Int,
) {
    Column(horizontalAlignment = Alignment.Start) {
        Text(recentLabel, style = MaterialTheme.typography.labelMedium)

        Spacer(Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            recentColors.take(recentColorsLimit).forEach { color ->
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(color)
                        .clickable {
                            controller.selectByColor(color, fromUser = true)
                        },
                )
            }
        }
    }
}
