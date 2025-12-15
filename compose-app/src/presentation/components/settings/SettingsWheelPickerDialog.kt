package presentation.components.settings

import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import clib.presentation.components.picker.model.ListPicker
import clib.presentation.components.settings.SettingsWheelPickerDialog
import com.alorma.compose.settings.ui.base.internal.LocalSettingsGroupEnabled
import com.alorma.compose.settings.ui.base.internal.SettingsTileColors
import com.alorma.compose.settings.ui.base.internal.SettingsTileDefaults
import data.type.primitives.string.asStringResource

@Suppress("ComposeParameterOrder")
@Composable
public fun <E> SettingsWheelPickerDialog(
    initialValue: E,
    values: List<E>,
    title: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = LocalSettingsGroupEnabled.current,
    icon: ImageVector? = null,
    subtitle: (@Composable () -> Unit)? = { Text(initialValue.asStringResource()) },
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
    picker: ListPicker = ListPicker(),
    onItemClicked: (E) -> Boolean,
): Unit = SettingsWheelPickerDialog(
    initialValue,
    values,
    { Text(title) },
    modifier,
    enabled,
    icon?.let { { Icon(it, initialValue.asStringResource()) } },
    subtitle,
    action,
    colors,
    tonalElevation,
    shadowElevation,
    semanticProperties,
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
    onItemClicked,
)
