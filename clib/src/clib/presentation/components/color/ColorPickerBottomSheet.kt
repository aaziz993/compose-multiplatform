package clib.presentation.components.color

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.SheetState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import clib.presentation.components.color.common.SelectedColorDetail
import clib.presentation.components.color.model.ColorPicker
import com.github.skydoves.colorpicker.compose.ColorPickerController
import com.github.skydoves.colorpicker.compose.rememberColorPickerController

/**
 * This created a bottom sheet to pick color. As a content of this user can select their color using
 * GridColorPicker or RGBAColorPicker.
 * GridColorPicker: This a color grid with predefined main 16 colors, and each color variances.
 * RGBAColorPicker: This a color picker with RGB and Alpha values. Consumer can create their own color by changing
 * RED, GREEN and BLUE values in a color.
 *
 * @param onDismissRequest: Executes when the user clicks outside of the bottom sheet, after sheet animates to Hidden.
 * @param onSelect: (selectedColor: Color) -> Unit: Callback to invoke when a color is selected.
 * @param onClose: Callback to invoke when user clicks close button.
 * @param controller [ColorPickerController].
 * @param sheetState: SheetState: State variable to control the bottom sheet.
 * @param picker: Picker visual configuration.
 *
 * @return @Composable: A bottom sheet UI.
 */
@Suppress("ComposeModifierMissing")
@Composable
public fun ColorPickerBottomSheet(
    onDismissRequest: () -> Unit,
    onSelect: (Color) -> Unit,
    onClose: () -> Unit = onDismissRequest,
    controller: ColorPickerController = rememberColorPickerController(),
    initialColor: Color = Color.White,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    picker: ColorPicker = ColorPicker()
): Unit = ModalBottomSheet(
    onDismissRequest = onDismissRequest,
    sheetState = sheetState,
    containerColor = MaterialTheme.colorScheme.background,
    scrimColor = MaterialTheme.colorScheme.onSurface.copy(alpha = .5f),
) {
    var color by remember { mutableStateOf(initialColor) }
    var hexColorChanged by remember { mutableStateOf(false) }
    var tabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf(
        picker.rgba,
        picker.grid,
        picker.hsv,
        picker.hsla,
        picker.blend,
    )

    Column(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(8.dp),
            )
            .padding(start = 12.dp, end = 12.dp),
    ) {
        Text(
            text = picker.title,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            fontSize = 36.sp,
        )

        PrimaryTabRow(
            modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 8.dp),
            selectedTabIndex = tabIndex,
            indicator = {
                TabRowDefaults.PrimaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabIndex),
                    color = MaterialTheme.colorScheme.secondary,
                    width = 150.dp,
                )
            },
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = {
                        Text(
                            text = title,
                            color = MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 12.sp,
                        )
                    },
                    selected = tabIndex == index,
                    onClick = { tabIndex = index },
                    selectedContentColor = MaterialTheme.colorScheme.secondary,
                )
            }
        }

        when (tabIndex) {
            0 -> RGBAColorPicker(
                color,
                { value ->
                    color = value
                },
                Modifier
                    .weight(.7f)
                    .padding(16.dp),
                picker.rgba,
                picker.red,
                picker.green,
                picker.blue,
                picker.alpha,
            )

            1 -> GridColorPicker(
                color,
                { value ->
                    color = value
                },
                modifier = Modifier
                    .weight(.7f)
                    .padding(16.dp),
                picker.grid,
            )

            2 -> HSVColorPicker(
                controller,
                color,
                { value ->
                    color = value
                },
                Modifier
                    .weight(.7f)
                    .padding(16.dp),
                picker.hsv,
                picker.brightness,
                picker.alpha,
                color,
            )

            3 -> HSLAColorPicker(
                hexColorChanged,
                color,
                { value ->
                    color = value
                },
                Modifier
                    .weight(.7f)
                    .padding(16.dp),
                picker.hsla,
                picker.saturation,
                picker.lightness,
                picker.alpha,
            )

            4 -> BlendColorPicker(
                hexColorChanged,
                color,
                { value ->
                    color = value
                },
                Modifier
                    .weight(.7f)
                    .padding(16.dp),
                picker.blend,
            )
        }

        Column(
            modifier = Modifier
                .shadow(
                    elevation = 10.dp,
                    shape = RoundedCornerShape(8.dp),
                )
                .padding(start = 12.dp, end = 12.dp, top = 12.dp, bottom = 12.dp)
                .weight(.3f),
        ) {
            SelectedColorDetail(
                color,
                {
                    color = it
                    hexColorChanged = !hexColorChanged
                    controller.selectByColor(it, true)
                },
                picker.hex,
                picker.copy,
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
            ) {
                OutlinedButton(
                    modifier = Modifier
                        .padding(start = 4.dp, end = 4.dp)
                        .weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    onClick = onClose,
                ) {
                    Text(
                        text = picker.close,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }

                Button(
                    modifier = Modifier
                        .padding(start = 4.dp, end = 4.dp)
                        .weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    onClick = { onSelect(color) },
                ) {
                    Text(
                        text = picker.confirm,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            }
        }
    }
}
