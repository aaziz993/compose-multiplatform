package clib.presentation.components.settings

import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.TimePickerDialogDefaults
import androidx.compose.material3.TimePickerDisplayMode
import androidx.compose.material3.rememberTimePickerState
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
import com.alorma.compose.settings.ui.SettingsMenuLink
import com.alorma.compose.settings.ui.base.internal.LocalSettingsGroupEnabled
import com.alorma.compose.settings.ui.base.internal.SettingsTileColors
import com.alorma.compose.settings.ui.base.internal.SettingsTileDefaults
import klib.data.type.primitives.time.now
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone

@Suppress("ComposeParameterOrder")
@Composable
public fun SettingsTimePickerDialog(
    title: @Composable () -> Unit,
    initialValue: LocalTime = LocalTime.now(TimeZone.currentSystemDefault()),
    modifier: Modifier = Modifier,
    enabled: Boolean = LocalSettingsGroupEnabled.current,
    icon: (@Composable () -> Unit)? = null,
    subtitle: (@Composable () -> Unit)? = null,
    action: (@Composable () -> Unit)? = null,
    colors: SettingsTileColors = SettingsTileDefaults.colors(),
    tonalElevation: Dp = SettingsTileDefaults.Elevation,
    shadowElevation: Dp = SettingsTileDefaults.Elevation,
    semanticProperties: (SemanticsPropertyReceiver.() -> Unit) = {},
    confirmButton: @Composable () -> Unit = {},
    dialogModifier: Modifier = Modifier,
    dialogProperties: DialogProperties = DialogProperties(),
    dismissButton: (@Composable () -> Unit)? = null,
    shape: Shape = TimePickerDialogDefaults.shape,
    containerColor: Color = TimePickerDialogDefaults.containerColor,
    onValueChanged: (LocalTime) -> Boolean,
) {
    var dialog by remember { mutableStateOf(false) }
    if (dialog) {
        var displayMode by remember { mutableStateOf(TimePickerDisplayMode.Picker) }
        val state = rememberTimePickerState(initialValue.hour, initialValue.minute, true)
        TimePickerDialog(
            {
                dialog = false
            },
            {

            },
            title,
            dialogModifier,
            dialogProperties,
            {
                TimePickerDialogDefaults.DisplayModeToggle(
                    {
                        when (displayMode) {
                            TimePickerDisplayMode.Picker -> displayMode = TimePickerDisplayMode.Input
                            TimePickerDisplayMode.Input -> displayMode = TimePickerDisplayMode.Picker
                        }
                    },
                    TimePickerDisplayMode.Picker,
                )
            },
            dismissButton,
            shape,
            containerColor,
        ) {
            when (displayMode) {
                TimePickerDisplayMode.Picker -> TimePicker(state)
                TimePickerDisplayMode.Input -> TimeInput(state)
            }
        }
    }

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
