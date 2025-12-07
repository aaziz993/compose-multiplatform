package clib.presentation.components.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import clib.presentation.components.model.item.Item
import clib.presentation.components.picker.ListPickerDialog
import clib.presentation.components.picker.model.Picker
import com.alorma.compose.settings.ui.base.internal.LocalSettingsGroupEnabled
import com.alorma.compose.settings.ui.base.internal.SettingsTileColors
import com.alorma.compose.settings.ui.base.internal.SettingsTileDefaults
import com.alorma.compose.settings.ui.base.internal.SettingsTileScaffold

@Composable
public fun <E> SettingsListPickerDialog(
    values: List<E>,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: @Composable (() -> Unit)? = null,
    icon: @Composable (() -> Unit)? = null,
    enabled: Boolean = LocalSettingsGroupEnabled.current,
    colors: SettingsTileColors = SettingsTileDefaults.colors(),
    tonalElevation: Dp = SettingsTileDefaults.Elevation,
    shadowElevation: Dp = SettingsTileDefaults.Elevation,
    item: (E) -> Item = { value -> Item(text = { Text(value.toString()) }) },
    textStyle: TextStyle = TextStyle(),
    itemPadding: Int = 10,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    picker: Picker = Picker(),
    onItemClicked: (E) -> Boolean,
) {
    var pickerDialog by remember { mutableStateOf(false) }
    if (pickerDialog)
        ListPickerDialog(
            values,
            { item ->
                pickerDialog = onItemClicked(item)
            },
            {
                pickerDialog = false
            },
            Modifier.padding(end = 16.dp),
            item,
            textStyle,
            itemPadding,
            backgroundColor,
            picker,
        )

    SettingsTileScaffold(
        modifier = modifier,
        enabled = enabled,
        title = title,
        subtitle = {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                subtitle?.invoke()
            }
        },
        icon = icon,
        colors = colors,
        tonalElevation = tonalElevation,
        shadowElevation = shadowElevation,
    )
}
