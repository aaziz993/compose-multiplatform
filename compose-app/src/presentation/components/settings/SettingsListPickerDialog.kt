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
import androidx.compose.ui.unit.Dp
import clib.presentation.components.model.item.Item
import clib.presentation.components.picker.model.ListPicker
import clib.presentation.components.settings.SettingsListPickerDialog
import com.alorma.compose.settings.ui.base.internal.LocalSettingsGroupEnabled
import com.alorma.compose.settings.ui.base.internal.SettingsTileColors
import com.alorma.compose.settings.ui.base.internal.SettingsTileDefaults
import compose_app.generated.resources.Res
import compose_app.generated.resources.clear
import compose_app.generated.resources.search
import data.type.primitives.string.asStringResource
import clib.data.type.primitives.string.stringResource

@Suppress("ComposeParameterOrder")
@Composable
public fun <E> SettingsListPickerDialog(
    value: E,
    values: List<E>,
    title: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = LocalSettingsGroupEnabled.current,
    icon: ImageVector? = null,
    subtitle: (@Composable () -> Unit)? = { Text(value.asStringResource()) },
    action: (@Composable () -> Unit)? = null,
    colors: SettingsTileColors = SettingsTileDefaults.colors(),
    tonalElevation: Dp = SettingsTileDefaults.Elevation,
    shadowElevation: Dp = SettingsTileDefaults.Elevation,
    semanticProperties: (SemanticsPropertyReceiver.() -> Unit) = {},
    item: (E) -> Item = { value ->
        Item(
            text = { Text(value.asStringResource()) },
            icon = { icon?.let { Icon(it, value.asStringResource()) } },
        )
    },
    textStyle: TextStyle = LocalTextStyle.current,
    itemPadding: Int = 10,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    picker: ListPicker = ListPicker(
        headerTitle = title,
        searchHint = stringResource(Res.string.search),
        clear = stringResource(Res.string.clear),
    ),
    onItemClicked: (E) -> Boolean,
): Unit = SettingsListPickerDialog(
    { Text(title) },
    values,
    modifier,
    enabled,
    icon?.let { { Icon(it, value.asStringResource()) } },
    subtitle,
    action,
    colors,
    tonalElevation,
    shadowElevation,
    semanticProperties,
    item,
    textStyle,
    itemPadding,
    backgroundColor,
    picker,
    onItemClicked,
)
