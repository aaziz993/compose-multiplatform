package clib.presentation.components.picker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import clib.presentation.components.model.item.Item
import clib.presentation.components.picker.model.Picker

@Composable
public fun <E> ListPickerDialog(
    values: List<E>,
    onItemClicked: (E) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    item: (E) -> Item = { value -> Item(text = { Text(value.toString()) }) },
    textStyle: TextStyle = TextStyle(),
    itemPadding: Int = 10,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    picker: Picker = Picker(),
): Unit = PickerDialog(
    values,
    onDismissRequest,
    modifier,
    textStyle,
    itemPadding,
    backgroundColor,
    picker,
) { values ->
    LazyColumn {
        items(values) { value ->
            HorizontalDivider(color = picker.dividerColor)
            ItemUI(
                item(value),
                {},
                textStyle,
                itemPadding,
            )
        }
    }
}

@Composable
private fun ItemUI(
    item: Item,
    onItemClicked: () -> Unit,
    itemTextStyle: TextStyle,
    itemPadding: Int = 10
) {
    Row(
        Modifier
            .clickable(onClick = onItemClicked)
            .padding(itemPadding.dp, (itemPadding * 1.5).dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        item.icon()
        item.text()
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewCountryPickerDialog() {
    val list = remember { (1..10).map { it.toString() } }
    ListPickerDialog(
        values = list,
        onItemClicked = {},
        onDismissRequest = {},
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .clip(RoundedCornerShape(10.dp)),
    )
}
