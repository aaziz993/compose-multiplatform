package clib.presentation.components.picker

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.window.DialogProperties
import clib.data.type.state.localTime
import clib.presentation.components.picker.model.TimePicker
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import pro.respawn.kmmutils.datetime.now

@Suppress("ComposeParameterOrder")
@Composable
public fun TimePickerDialog(
    onDismissRequest: () -> Unit,
    onClose: () -> Unit = onDismissRequest,
    value: LocalTime = LocalTime.now(TimeZone.currentSystemDefault()),
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    dialogProperties: DialogProperties = DialogProperties(),
    shape: Shape = TimePickerDialogDefaults.shape,
    containerColor: Color = TimePickerDialogDefaults.containerColor,
    picker: TimePicker = TimePicker(),
    onValueChanged: (LocalTime) -> Unit,
) {
    val state = rememberTimePickerState(value.hour, value.minute, picker.is24Hour)
    var displayMode by remember { mutableStateOf(TimePickerDisplayMode.Picker) }
    TimePickerDialog(
        onDismissRequest,
        {
            IconButton(
                onClick = {
                    onValueChanged(state.localTime)
                },
            ) {
                Icon(Icons.Default.Check, picker.confirm)
            }
        },
        title,
        modifier,
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
        {
            IconButton(
                onClick = onClose,
            ) {
                Icon(Icons.Default.Close, picker.close)
            }
        },
        shape,
        containerColor,
    ) {
        when (displayMode) {
            TimePickerDisplayMode.Picker -> TimePicker(state)
            TimePickerDisplayMode.Input -> TimeInput(state)
        }
    }
}
