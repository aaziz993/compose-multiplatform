package clib.presentation.color

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import clib.presentation.color.common.SelectedColorDetail
import clib.presentation.color.model.ColorPicker
import com.materialkolor.ktx.toHex

/**
 * This created a bottom sheet to pick color. As a content of this user can select their color using
 * GridColorPicker or RGBAColorPicker.
 * GridColorPicker: This a color grid with predefined main 16 colors, and each color variances.
 * RGBAColorPicker: This a color picker with RGB and Alpha values. Consumer can create their own color by changing
 * RED, GREEN and BLUE values in a color.
 *
 * @param lastSelectedColor: Color: variable to pass last selected color.
 * @param showSheet: MutableState<Boolean>: State variable to show and hide bottom sheet.
 * @param sheetState: SheetState: State variable to control the bottom sheet.
 * @param onColorSelected: (selectedColor: Color) -> Unit: Callback to invoke when a color is selected.
 *
 * @return @Composable: A bottom sheet UI.
 */
@Suppress("ComposeModifierMissing")
@Composable
public fun ColorPickerBottomSheet(
    sheetState: SheetState,
    showSheet: MutableState<Boolean>,
    onColorSelected: (selectedColor: Color) -> Unit,
    lastSelectedColor: Color = Color.White,
    picker: ColorPicker = ColorPicker()
) {
    ModalBottomSheet(
        onDismissRequest = {
            showSheet.value = false
        },
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.background,
        scrimColor = MaterialTheme.colorScheme.onSurface.copy(alpha = .5f),
    ) {
        Column {
            var selectedColor by remember { mutableStateOf(lastSelectedColor) }
            val colorHex = remember { mutableStateOf(TextFieldValue("#000000")) }
            var tabIndex by remember { mutableIntStateOf(0) }
            val tabs = listOf(
                picker.rgbaLabel,
                picker.gridLabel,
                picker.hslaLabel,
                picker.blendLabel,
            )

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
                containerColor = Color.Transparent,
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
                    modifier = Modifier.padding(16.dp),
                    lastSelectedColor = selectedColor,
                    onColorSelected = {
                        selectedColor = it
                        colorHex.value = TextFieldValue(it.toHex())
                    },
                )

                1 -> GridColorPicker(
                    modifier = Modifier.padding(16.dp),
                    lastSelectedColor = selectedColor,
                    onColorSelected = {
                        selectedColor = it
                        colorHex.value = TextFieldValue(it.toHex())
                    },
                )

                2 -> HSLAColorPicker(
                    modifier = Modifier.padding(16.dp),
                    lastSelectedColor = selectedColor,
                    onColorSelected = {
                        selectedColor = it
                        colorHex.value = TextFieldValue(it.toHex())
                    },
                )

                3 -> HSVColorPicker(
                    modifier = Modifier.padding(16.dp),
                    lastSelectedColor = selectedColor,
                    onColorSelected = {
                        selectedColor = it
                        colorHex.value = TextFieldValue(it.toHex())
                    },
                )

                4 -> BlendColorPicker(
                    modifier = Modifier.padding(16.dp),
                    onColorSelected = {
                        selectedColor = it
                        colorHex.value = TextFieldValue(it.toHex())
                    },
                )
            }

            Column(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
                    .border(1.dp, Color.White, shape = RoundedCornerShape(8.dp))
                    .shadow(
                        elevation = 10.dp,
                        shape = RoundedCornerShape(8.dp),
                    )
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(start = 12.dp, end = 12.dp),
            ) {
                SelectedColorDetail(
                    picker.selectedColorTitle,
                    picker.hexLabel,
                    selectedColor,
                    colorHex,
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
                        onClick = {
                            showSheet.value = false
                        },
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
                        onClick = {
                            showSheet.value = false
                            onColorSelected.invoke(selectedColor)
                        },
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
}
