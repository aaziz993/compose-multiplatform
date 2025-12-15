package clib.presentation.components.picker

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import clib.presentation.components.picker.model.ListPicker

@Composable
public fun <E> WheelPickerDialog(
    onDismissRequest: () -> Unit,
    initialValue: E,
    values: List<E>,
    onItemClicked: (E) -> Unit,
    modifier: Modifier = Modifier,
    itemPadding: Int = 10,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    wrapSelectorWheel: Boolean = false,
    format: E.() -> String = { toString() },
    parse: (String.() -> E?)? = null,
    onIsErrorChange: (Boolean) -> Unit = {},
    enableEdition: Boolean = parse != null,
    beyondViewportPageCount: Int = 1,
    textStyle: TextStyle = LocalTextStyle.current,
    verticalPadding: Dp = 16.dp,
    dividerColor: Color = MaterialTheme.colorScheme.outline,
    dividerThickness: Dp = 1.dp,
    keyboardType: KeyboardType = KeyboardType.Text,
    picker: ListPicker = ListPicker(),
): Unit = PickerDialog(
    values,
    onDismissRequest,
    modifier,
    textStyle,
    itemPadding,
    backgroundColor,
    picker,
) { values ->
    WheelPicker(
        initialValue = initialValue,
        values = values,
        onValueChange = onItemClicked,
        wrapSelectorWheel = wrapSelectorWheel,
        format = format,
        parse = parse,
        onIsErrorChange = onIsErrorChange,
        enableEdition = enableEdition,
        beyondViewportPageCount = beyondViewportPageCount,
        textStyle = textStyle,
        verticalPadding = verticalPadding,
        dividerColor = dividerColor,
        dividerThickness = dividerThickness,
        keyboardType = keyboardType,
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewCountryPickerDialog() {
    var value by remember { mutableStateOf("5") }
    val list = remember { (1..10).map { it.toString() } }
    WheelPickerDialog(
        initialValue = value,
        values = list,
        onItemClicked = {},
        onDismissRequest = {},
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .clip(RoundedCornerShape(10.dp)),
    )
}
