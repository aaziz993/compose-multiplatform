package clib.presentation.components.color

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.SheetState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import clib.presentation.components.color.model.ColorPicker
import com.ashampoo.kim.format.bmff.box.Box

/**
 * This created a bottom sheet to pick color. As a content of this user can select their color using
 * GridColorPicker or RGBAColorPicker.
 * GridColorPicker: This a color grid with predefined main 16 colors, and each color variances.
 * RGBAColorPicker: This a color picker with RGB and Alpha values. Consumer can create their own color by changing
 * RED, GREEN and BLUE values in a color.
 *
 * @param onDismissRequest: Executes when the user clicks outside of the bottom sheet, after sheet animates to Hidden.
 * @param sheetState: SheetState: State variable to control the bottom sheet.
 * @param picker: Picker visual configuration.
 *
 * @return @Composable: A bottom sheet UI.
 */
@Suppress("ComposeParameterOrder")
@Composable
public fun ColorPickerBottomSheet(
    state: MutableState<Color>,
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    title: @Composable () -> Unit = {},
    modifier: Modifier = Modifier,
    dismissButton: (@Composable () -> Unit)? = null,
    sheetState: SheetState = rememberModalBottomSheetState(),
    picker: ColorPicker = ColorPicker(),
): Unit = ModalBottomSheet(
    onDismissRequest = onDismissRequest,
    sheetState = sheetState,
    containerColor = MaterialTheme.colorScheme.background,
    scrimColor = MaterialTheme.colorScheme.onSurface.copy(alpha = .5f),
) {
    var tabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf(
        picker.rgba,
        picker.grid,
        picker.hsv,
        picker.hsla,
        picker.blend,
    )

    Column(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            title()
        }

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
                            overflow = TextOverflow.Clip,
                            maxLines = 1,
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
                state.value,
                { value ->
                    state.value = value
                },
                Modifier
                    .weight(.8f)
                    .padding(16.dp),
                picker.rgba,
                picker.red,
                picker.green,
                picker.blue,
                picker.alpha,
                picker.hex,
                picker.copy,
            )

            1 -> GridColorPicker(
                state.value,
                { value ->
                    state.value = value
                },
                modifier = Modifier
                    .weight(.8f)
                    .padding(16.dp),
                picker.grid,
                picker.hex,
                picker.copy,
            )

            2 -> HSVColorPicker(
                state.value,
                { value ->
                    state.value = value
                },
                Modifier
                    .weight(.8f)
                    .padding(16.dp),
                picker.hsv,
                picker.brightness,
                picker.alpha,
                picker.hex,
                picker.copy,
            )

            3 -> HSLAColorPicker(
                state.value,
                { value ->
                    state.value = value
                },
                Modifier
                    .weight(.8f)
                    .padding(16.dp),
                picker.hsla,
                picker.saturation,
                picker.lightness,
                picker.alpha,
                picker.hex,
                picker.copy,
            )

            4 -> BlendColorPicker(
                state.value,
                { value ->
                    state.value = value
                },
                Modifier
                    .weight(.8f)
                    .padding(16.dp),
                picker.blend,
                picker.left,
                picker.right,
                picker.hex,
                picker.copy,
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(.2f)
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            dismissButton?.invoke()
            confirmButton()
        }
    }
}
