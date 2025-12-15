package clib.presentation.components.settings

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import clib.presentation.components.color.ColorPickerBottomSheet
import clib.presentation.components.color.model.ColorPicker
import com.alorma.compose.settings.ui.SettingsMenuLink
import com.alorma.compose.settings.ui.base.internal.LocalSettingsGroupEnabled
import com.alorma.compose.settings.ui.base.internal.SettingsTileColors
import com.alorma.compose.settings.ui.base.internal.SettingsTileDefaults
import com.materialkolor.ktx.toHex

@Composable
public fun SettingsColorPickerBottomSheet(
    title: @Composable () -> Unit,
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
    picker: ColorPicker = ColorPicker(),
    onValueChanged: (Color) -> Boolean,
) {
    var sheet by remember { mutableStateOf(false) }
    if (sheet)
        ColorPickerBottomSheet(
            { sheet = false },
            { value ->
                sheet = onValueChanged(value)
            },
            initialColor = value,
            sheetState = sheetState,
            picker = picker,
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
        sheet = true
    }
}
