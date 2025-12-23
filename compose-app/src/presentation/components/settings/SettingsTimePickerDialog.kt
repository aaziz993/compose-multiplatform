package presentation.components.settings

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TimePickerColors
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerDialogDefaults
import androidx.compose.material3.TimePickerLayoutType
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.DialogProperties
import clib.data.type.primitives.string.stringResource
import clib.data.type.state.localDate
import clib.data.type.state.localTime
import clib.presentation.components.settings.SettingsTimePickerDialog
import com.alorma.compose.settings.ui.base.internal.LocalSettingsGroupEnabled
import com.alorma.compose.settings.ui.base.internal.SettingsTileColors
import com.alorma.compose.settings.ui.base.internal.SettingsTileDefaults
import compose_app.generated.resources.Res
import compose_app.generated.resources.close
import compose_app.generated.resources.confirm
import klib.data.type.primitives.time.toEpochMilliseconds
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

@Suppress("ComposeParameterOrder")
@Composable
public fun SettingsTimePickerDialog(
    title: String,
    date: LocalDate? = null,
    time: LocalTime? = null,
    modifier: Modifier = Modifier,
    enabled: Boolean = LocalSettingsGroupEnabled.current,
    icon: ImageVector? = Icons.Default.Watch,
    subtitle: (@Composable () -> Unit)? = null,
    action: (@Composable () -> Unit)? = null,
    colors: SettingsTileColors = SettingsTileDefaults.colors(),
    tonalElevation: Dp = SettingsTileDefaults.Elevation,
    shadowElevation: Dp = SettingsTileDefaults.Elevation,
    semanticProperties: (SemanticsPropertyReceiver.() -> Unit) = {},
    dialogModifier: Modifier = Modifier,
    properties: DialogProperties = DialogProperties(),
    showModeToggle: Boolean = true,
    shape: Shape = TimePickerDialogDefaults.shape,
    containerColor: Color = TimePickerDialogDefaults.containerColor,
    dateFormatter: DatePickerFormatter = DatePickerDefaults.dateFormatter(),
    datePickerColors: DatePickerColors = DatePickerDefaults.colors(),
    dateTitle: (@Composable () -> Unit)? = null,
    dateHeadline: (@Composable () -> Unit)? = null,
    dateFocusRequester: FocusRequester? = null,
    timePickerColors: TimePickerColors = TimePickerDefaults.colors(),
    timePickerLayoutType: TimePickerLayoutType = TimePickerDefaults.layoutType(),
    onValueChanged: (LocalDate?, LocalTime?) -> Boolean,
) {
    val dateState = date?.let { rememberDatePickerState(initialSelectedDateMillis = it.toEpochMilliseconds()) }
    val timeState = time?.let { rememberTimePickerState(it.hour, it.minute, true) }
    SettingsTimePickerDialog(
        {
            Text(
                text = title,
                overflow = TextOverflow.Clip,
                maxLines = 1,
            )
        },
        dateState,
        timeState,
        { dismiss ->
            IconButton(
                onClick = {
                    if (!onValueChanged(dateState?.localDate, timeState?.localTime)) dismiss()
                },
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = stringResource(Res.string.confirm),
                    tint = Color.Green,
                )
            }
        },
        modifier,
        enabled,
        icon?.let { { Icon(it, title) } },
        subtitle,
        action,
        colors,
        tonalElevation,
        shadowElevation,
        semanticProperties,
        dialogModifier,
        properties,
        showModeToggle,
        { dismiss ->
            IconButton(dismiss) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(Res.string.close),
                    tint = MaterialTheme.colorScheme.error,
                )
            }
        },
        shape,
        containerColor,
        dateFormatter,
        datePickerColors,
        dateTitle,
        dateHeadline,
        dateFocusRequester,
        timePickerColors,
        timePickerLayoutType,
    )
}
