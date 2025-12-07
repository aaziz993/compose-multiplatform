package presentation.components.settings

import androidx.compose.material3.Icon
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.unit.Dp
import com.alorma.compose.settings.ui.SettingsSwitch
import com.alorma.compose.settings.ui.base.internal.LocalSettingsGroupEnabled
import com.alorma.compose.settings.ui.base.internal.SettingsTileColors
import com.alorma.compose.settings.ui.base.internal.SettingsTileDefaults
import data.type.primitives.asStringResource

@Suppress("ComposeParameterOrder")
@Composable
public fun SettingsSwitch(
    title: String,
    value: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = LocalSettingsGroupEnabled.current,
    trueIcon: ImageVector,
    falseIcon: ImageVector,
    colors: SettingsTileColors = SettingsTileDefaults.colors(),
    switchColors: SwitchColors =
        SwitchDefaults.colors(
            checkedTrackColor = colors.actionColor(enabled),
            checkedThumbColor = contentColorFor(colors.actionColor(enabled)),
            disabledCheckedTrackColor = colors.actionColor(enabled),
            disabledCheckedThumbColor = contentColorFor(colors.actionColor(enabled)),
        ),
    tonalElevation: Dp = SettingsTileDefaults.Elevation,
    shadowElevation: Dp = SettingsTileDefaults.Elevation,
    semanticProperties: (SemanticsPropertyReceiver.() -> Unit) = {},
    onCheckedChange: (Boolean) -> Unit,
): Unit = SettingsSwitch(
    value,
    { Text(title) },
    modifier,
    enabled,
    { Icon(if (value) trueIcon else falseIcon, title) },
    { Text(value.asStringResource()) },
    colors,
    switchColors,
    tonalElevation,
    shadowElevation,
    semanticProperties,
    onCheckedChange,
)
