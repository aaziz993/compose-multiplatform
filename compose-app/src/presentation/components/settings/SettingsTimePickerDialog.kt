package presentation.components.settings

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TimePickerDialogDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.DialogProperties
import clib.presentation.components.settings.SettingsTimePickerDialog
import com.alorma.compose.settings.ui.base.internal.LocalSettingsGroupEnabled
import com.alorma.compose.settings.ui.base.internal.SettingsTileColors
import com.alorma.compose.settings.ui.base.internal.SettingsTileDefaults
import klib.data.type.primitives.time.now
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone

@Suppress("ComposeParameterOrder")
@Composable
public fun SettingsTimePickerDialog(
    title: String,
    initialValue: LocalTime = LocalTime.now(TimeZone.currentSystemDefault()),
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
    dialogProperties: DialogProperties = DialogProperties(),
    modeToggleButton: (@Composable () -> Unit)? = null,
    dismissButton: (@Composable () -> Unit)? = null,
    shape: Shape = TimePickerDialogDefaults.shape,
    containerColor: Color = TimePickerDialogDefaults.containerColor,
    onValueChanged: (LocalTime) -> Boolean,
): Unit = SettingsTimePickerDialog(
    title = { Text(title) },
    initialValue,
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
    dialogProperties,
    modeToggleButton,
    dismissButton,
    shape,
    containerColor,
    onValueChanged,
)
