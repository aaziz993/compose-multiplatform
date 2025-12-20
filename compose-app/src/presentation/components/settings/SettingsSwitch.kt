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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import clib.presentation.events.snackbar.GlobalSnackbarEventController
import clib.presentation.events.snackbar.model.SnackbarEvent
import com.alorma.compose.settings.ui.SettingsSwitch
import com.alorma.compose.settings.ui.base.internal.LocalSettingsGroupEnabled
import com.alorma.compose.settings.ui.base.internal.SettingsTileColors
import com.alorma.compose.settings.ui.base.internal.SettingsTileDefaults
import data.type.primitives.asStringResource
import klib.data.permission.exception.PermissionDeniedAlwaysException
import klib.data.permission.exception.PermissionDeniedException
import klib.data.permission.model.Permission

@Composable
public fun SettingsSwitch(
    title: String,
    value: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = LocalSettingsGroupEnabled.current,
    trueIcon: ImageVector? = null,
    falseIcon: ImageVector? = null,
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
    {
        Text(
            text = title,
            overflow = TextOverflow.Clip,
            maxLines = 1,
        )
    },
    modifier,
    enabled,
    (if (value) trueIcon else falseIcon)?.let { { Icon(it, value.asStringResource()) } },
    { Text(value.asStringResource()) },
    colors,
    switchColors,
    tonalElevation,
    shadowElevation,
    semanticProperties,
    onCheckedChange,
)

@Suppress("ComposeParameterOrder")
@Composable
public fun SettingsSwitch(
    title: String,
    value: Permission,
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
    permissions: Set<Permission>,
    onCheckedChange: (Permission?) -> Unit,
): Unit = SettingsSwitch(
    title,
    value in permissions,
    modifier,
    enabled,
    trueIcon,
    falseIcon,
    colors,
    switchColors,
    tonalElevation,
    shadowElevation,
    semanticProperties,
    onCheckedChange = {
        if (it) onCheckedChange(null)
        else try {
            onCheckedChange(value)
        }
        catch (deniedAlways: PermissionDeniedAlwaysException) {
            GlobalSnackbarEventController.sendEvent(SnackbarEvent(deniedAlways.message.orEmpty()))
        }
        catch (denied: PermissionDeniedException) {
            GlobalSnackbarEventController.sendEvent(SnackbarEvent(denied.message.orEmpty()))
        }
    },
)
