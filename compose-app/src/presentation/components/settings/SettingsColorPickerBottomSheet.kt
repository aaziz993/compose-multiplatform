package presentation.components.settings

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import clib.data.type.primitives.string.stringResource
import clib.presentation.components.color.model.ColorPicker
import clib.presentation.components.settings.SettingsColorPickerBottomSheet
import com.alorma.compose.settings.ui.base.internal.LocalSettingsGroupEnabled
import com.alorma.compose.settings.ui.base.internal.SettingsTileColors
import com.alorma.compose.settings.ui.base.internal.SettingsTileDefaults
import com.materialkolor.ktx.toHex
import compose_app.generated.resources.Res
import compose_app.generated.resources.alpha
import compose_app.generated.resources.blend
import compose_app.generated.resources.blue
import compose_app.generated.resources.brightness
import compose_app.generated.resources.close
import compose_app.generated.resources.confirm
import compose_app.generated.resources.copy
import compose_app.generated.resources.green
import compose_app.generated.resources.grid
import compose_app.generated.resources.hex
import compose_app.generated.resources.hsla
import compose_app.generated.resources.hsv
import compose_app.generated.resources.left
import compose_app.generated.resources.lightness
import compose_app.generated.resources.red
import compose_app.generated.resources.rgba
import compose_app.generated.resources.right
import compose_app.generated.resources.saturation

@Suppress("ComposeParameterOrder")
@Composable
public fun SettingsColorPickerBottomSheet(
    title: String,
    value: Color,
    modifier: Modifier = Modifier,
    enabled: Boolean = LocalSettingsGroupEnabled.current,
    subtitle: (@Composable () -> Unit)? = { Text(text = value.toHex()) },
    icon: (@Composable () -> Unit)? = {
        Icon(
            imageVector = Icons.Filled.Circle,
            contentDescription = value.toHex(),
            Modifier.border(
                2.dp,
                MaterialTheme.colorScheme.onSurface,
                CircleShape,
            ),
            tint = value,
        )
    },
    action: (@Composable () -> Unit)? = null,
    colors: SettingsTileColors = SettingsTileDefaults.colors(),
    tonalElevation: Dp = SettingsTileDefaults.Elevation,
    shadowElevation: Dp = SettingsTileDefaults.Elevation,
    semanticProperties: (SemanticsPropertyReceiver.() -> Unit) = {},
    sheetModifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    picker: ColorPicker = ColorPicker(
        stringResource(Res.string.rgba),
        stringResource(Res.string.red),
        stringResource(Res.string.green),
        stringResource(Res.string.blue),
        stringResource(Res.string.alpha),
        stringResource(Res.string.grid),
        stringResource(Res.string.hsla),
        stringResource(Res.string.saturation),
        stringResource(Res.string.lightness),
        stringResource(Res.string.hsv),
        stringResource(Res.string.brightness),
        stringResource(Res.string.blend),
        stringResource(Res.string.left),
        stringResource(Res.string.right),
        stringResource(Res.string.hex),
        stringResource(Res.string.copy),
    ),
    onValueChanged: (Color) -> Boolean,
) {
    val state = remember { mutableStateOf(value) }
    SettingsColorPickerBottomSheet(
        {
            Text(
                text = title,
                overflow = TextOverflow.Clip,
                maxLines = 1,
            )
        },
        state,
        { dismiss ->
            IconButton(
                onClick = {
                    if (!onValueChanged(state.value)) dismiss()
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
        subtitle,
        icon,
        action,
        colors,
        tonalElevation,
        shadowElevation,
        semanticProperties,
        sheetModifier,
        { dismiss ->
            IconButton(dismiss) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(Res.string.close),
                    tint = MaterialTheme.colorScheme.error,
                )
            }
        },
        sheetState,
        picker,
    )
}
