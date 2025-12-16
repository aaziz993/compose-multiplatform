package clib.presentation.components.picker

import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.TimePickerDialogDefaults
import androidx.compose.material3.TimePickerDisplayMode
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.window.DialogProperties

@Suppress("ComposeParameterOrder")
@Composable
public fun TimePickerDialog(
    state: TimePickerState,
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit = {},
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    dialogProperties: DialogProperties = DialogProperties(),
    dismissButton: (@Composable () -> Unit)? = null,
    shape: Shape = TimePickerDialogDefaults.shape,
    containerColor: Color = TimePickerDialogDefaults.containerColor,
) {
    var displayMode by remember { mutableStateOf(TimePickerDisplayMode.Picker) }
    TimePickerDialog(
        onDismissRequest,
        confirmButton,
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
