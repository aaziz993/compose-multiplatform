package presentation.components.settings

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
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
import compose_app.generated.resources.cancel
import compose_app.generated.resources.color
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
import compose_app.generated.resources.select
import clib.data.type.primitives.string.stringResource

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
    sheetState: SheetState = rememberModalBottomSheetState(),
    picker: ColorPicker = ColorPicker(
        stringResource(Res.string.color),
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
        stringResource(Res.string.cancel),
        stringResource(Res.string.select),
    ),
    onValueChanged: (Color) -> Unit,
): Unit = SettingsColorPickerBottomSheet(
    { Text(title) },
    value,
    modifier,
    enabled,
    subtitle,
    icon,
    action,
    colors,
    tonalElevation,
    shadowElevation,
    semanticProperties,
    sheetState,
    picker,
) { value ->
    onValueChanged(value)
    false
}
