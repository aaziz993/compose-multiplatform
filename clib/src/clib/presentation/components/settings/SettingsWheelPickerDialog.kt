package clib.presentation.components.settings

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import clib.presentation.components.picker.WheelPickerDialog
import clib.presentation.components.picker.model.Picker
import com.alorma.compose.settings.ui.SettingsMenuLink
import com.alorma.compose.settings.ui.base.internal.LocalSettingsGroupEnabled
import com.alorma.compose.settings.ui.base.internal.SettingsTileColors
import com.alorma.compose.settings.ui.base.internal.SettingsTileDefaults

@Composable
public fun <E> SettingsWheelPickerDialog(
    initialValue: E,
    values: List<E>,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = LocalSettingsGroupEnabled.current,
    icon: (@Composable () -> Unit)? = null,
    subtitle: (@Composable () -> Unit)? = null,
    action: (@Composable () -> Unit)? = null,
    colors: SettingsTileColors = SettingsTileDefaults.colors(),
    tonalElevation: Dp = SettingsTileDefaults.Elevation,
    shadowElevation: Dp = SettingsTileDefaults.Elevation,
    semanticProperties: (SemanticsPropertyReceiver.() -> Unit) = {},
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
    picker: Picker = Picker(),
    onItemClicked: (E) -> Boolean,
) {
    var pickerDialog by remember { mutableStateOf(false) }
    if (pickerDialog)
        WheelPickerDialog(
            initialValue,
            values,
            { value ->
                pickerDialog = onItemClicked(value)
            },
            {
                pickerDialog = false
            },
            Modifier.padding(end = 16.dp),
            itemPadding,
            backgroundColor,
            wrapSelectorWheel,
            format,
            parse,
            onIsErrorChange,
            enableEdition,
            beyondViewportPageCount,
            textStyle,
            verticalPadding,
            dividerColor,
            dividerThickness,
            keyboardType,
            picker,
        )

    SettingsMenuLink(
        title,
        modifier,
        enabled,
        icon,
        subtitle,
        action,
        colors,
        tonalElevation,
        shadowElevation,
        semanticProperties,
    ) {
        pickerDialog = true
    }
}
