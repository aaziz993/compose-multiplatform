package clib.presentation.components.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
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

@Composable
public fun SettingsColorMenuLink(
    value: Color,
    onValueChanged: (Color) -> Unit,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = LocalSettingsGroupEnabled.current,
    subtitle: (@Composable () -> Unit)? = null,
    action: (@Composable () -> Unit)? = null,
    colors: SettingsTileColors = SettingsTileDefaults.colors(),
    tonalElevation: Dp = SettingsTileDefaults.Elevation,
    shadowElevation: Dp = SettingsTileDefaults.Elevation,
    semanticProperties: (SemanticsPropertyReceiver.() -> Unit) = {},
    sheetState: SheetState = rememberModalBottomSheetState(),
    picker: ColorPicker = ColorPicker(),
) {

    var showSheet by remember { mutableStateOf(false) }

    if (showSheet)
        ColorPickerBottomSheet(
            { showSheet = false },
            { value ->
                onValueChanged(value)
                showSheet = false
            },
            initialColor = value,
            sheetState = sheetState,
            picker = picker,
        )

    SettingsMenuLink(
        title,
        modifier,
        enabled,
        {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(value, MaterialTheme.shapes.small)
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.onSurface,
                        MaterialTheme.shapes.small,
                    ),
            )
        },
        subtitle,
        action,
        colors,
        tonalElevation,
        shadowElevation,
        semanticProperties,
        {
            showSheet = true
        },
    )
}
