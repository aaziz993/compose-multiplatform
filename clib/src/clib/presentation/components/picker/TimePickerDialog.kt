package clib.presentation.components.picker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerColors
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.TimePickerDialogDefaults
import androidx.compose.material3.TimePickerDisplayMode
import androidx.compose.material3.TimePickerLayoutType
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.window.DialogProperties

@Suppress("ComposeParameterOrder")
@Composable
public fun TimePickerDialog(
    datePickerState: DatePickerState? = null,
    timePickerState: TimePickerState? = null,
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit = {},
    title: @Composable () -> Unit = {},
    modifier: Modifier = Modifier,
    properties: DialogProperties = DialogProperties(),
    showModeToggle: Boolean = true,
    dismissButton: (@Composable () -> Unit)? = null,
    shape: Shape = TimePickerDialogDefaults.shape,
    containerColor: Color = TimePickerDialogDefaults.containerColor,
    dateFormatter: DatePickerFormatter = DatePickerDefaults.dateFormatter(),
    datePickerColors: DatePickerColors = DatePickerDefaults.colors(),
    dateTitle: (@Composable () -> Unit)? = null,
    dateHeadline: (@Composable () -> Unit)? = null,
    dateFocusRequester: FocusRequester? = null,
    timePickerColors: TimePickerColors = TimePickerDefaults.colors(),
    timePickerLayoutType: TimePickerLayoutType = TimePickerDefaults.layoutType(),
) {
    var displayMode by remember { mutableStateOf(TimePickerDisplayMode.Picker) }
    TimePickerDialog(
        onDismissRequest,
        confirmButton,
        title,
        modifier,
        properties,
        if (showModeToggle && timePickerState != null) {
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
            }
        }
        else null,
        dismissButton,
        shape,
        containerColor,
    ) {
        Row(
            Modifier.fillMaxWidth(),
            Arrangement.SpaceAround,
            Alignment.CenterVertically,
        ) {
            datePickerState?.let {
                DatePicker(
                    it,
                    Modifier.weight(1f),
                    dateFormatter,
                    datePickerColors,
                    dateTitle,
                    dateHeadline,
                    showModeToggle,
                    dateFocusRequester,
                )
            }
            timePickerState?.let {
                when (displayMode) {
                    TimePickerDisplayMode.Picker -> TimePicker(
                        it,
                        Modifier.weight(1f),
                        timePickerColors,
                        timePickerLayoutType,
                    )

                    TimePickerDisplayMode.Input -> TimeInput(
                        it,
                        Modifier.weight(1f),
                        timePickerColors,
                    )
                }
            }
        }
    }
}
