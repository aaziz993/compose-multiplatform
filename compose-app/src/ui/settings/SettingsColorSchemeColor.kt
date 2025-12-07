package ui.settings

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import clib.presentation.components.color.model.ColorPicker
import clib.presentation.components.settings.SettingsColorPickerBottomSheet
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
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun SettingsColorSchemeColor(
    title: String,
    value: Color,
    onValueChanged: (Color) -> Unit,
): Unit = SettingsColorPickerBottomSheet(
    value = value,
    onValueChanged = { value ->
        onValueChanged(value)
        false
    },
    title = { Text(text = title) },
    modifier = Modifier,
    enabled = true,
    picker = ColorPicker(
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
)
