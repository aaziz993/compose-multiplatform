package clib.presentation.components.settings

import androidx.compose.material3.TimePickerDialogDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.DialogProperties
import clib.presentation.components.picker.TimePickerDialog
import clib.presentation.components.picker.model.TimePicker
import com.alorma.compose.settings.ui.SettingsMenuLink
import com.alorma.compose.settings.ui.base.internal.LocalSettingsGroupEnabled
import com.alorma.compose.settings.ui.base.internal.SettingsTileColors
import com.alorma.compose.settings.ui.base.internal.SettingsTileDefaults

@Suppress("ComposeParameterOrder")
@Composable
public fun SettingsTimePickerDialog(
    title: @Composable () -> Unit,
    onConfirm: () -> Unit,
    state: TimePickerState,
    modifier: Modifier = Modifier,
    enabled: Boolean = LocalSettingsGroupEnabled.current,
    icon: (@Composable () -> Unit)? = null,
    subtitle: (@Composable () -> Unit)? = null,
    action: (@Composable () -> Unit)? = null,
    colors: SettingsTileColors = SettingsTileDefaults.colors(),
    tonalElevation: Dp = SettingsTileDefaults.Elevation,
    shadowElevation: Dp = SettingsTileDefaults.Elevation,
    semanticProperties: (SemanticsPropertyReceiver.() -> Unit) = {},
    dialogModifier: Modifier = Modifier,
    dialogProperties: DialogProperties = DialogProperties(),
    shape: Shape = TimePickerDialogDefaults.shape,
    containerColor: Color = TimePickerDialogDefaults.containerColor,
    picker: TimePicker = TimePicker(),
) {
    var dialog by remember { mutableStateOf(false) }
    if (dialog)
        TimePickerDialog(
            onDismissRequest = {
                dialog = false
            },
            onConfirm = onConfirm,
            state = state,
            title = title,
            modifier = dialogModifier,
            dialogProperties = dialogProperties,
            shape = shape,
            containerColor = containerColor,
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
        dialog = true
    }
}
