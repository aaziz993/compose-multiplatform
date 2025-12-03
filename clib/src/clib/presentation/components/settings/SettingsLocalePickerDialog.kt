package clib.presentation.components.settings

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import clib.data.location.country.getEmojiFlag
import clib.presentation.components.country.LocalePickerDialog
import clib.presentation.components.country.model.CountryPicker
import com.alorma.compose.settings.ui.SettingsMenuLink
import com.alorma.compose.settings.ui.base.internal.LocalSettingsGroupEnabled
import com.alorma.compose.settings.ui.base.internal.SettingsTileColors
import com.alorma.compose.settings.ui.base.internal.SettingsTileDefaults
import klib.data.location.locale.Locale

@Composable
public fun SettingsLocalePickerDialog(
    value: Locale,
    onValueChanged: (Locale) -> Unit,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = LocalSettingsGroupEnabled.current,
    icon: (@Composable () -> Unit)? = null,
    subtitle: (@Composable () -> Unit)? = null,
    action: (@Composable () -> Unit)? = null,
    colors: SettingsTileColors = SettingsTileDefaults.colors(),
    tonalElevation: Dp = SettingsTileDefaults.Elevation,
    shadowElevation: Dp = SettingsTileDefaults.Elevation,
    semanticProperties: (SemanticsPropertyReceiver.() -> Unit) = {},
    locales: List<Locale> = Locale.getLocales().toList(),
    textStyle: TextStyle = TextStyle(),
    itemPadding: Int = 10,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    picker: CountryPicker = CountryPicker(),
) {
    var isLocalePickerDialogOpen by remember { mutableStateOf(false) }
    if (isLocalePickerDialogOpen)
        LocalePickerDialog(
            onItemClicked = onValueChanged,
            onDismissRequest = {
                isLocalePickerDialogOpen = false
            },
            locales = locales,
            picker = picker,
        )

    SettingsMenuLink(
        title,
        modifier,
        enabled,
        { Text(value.country()!!.alpha2.getEmojiFlag()) },
        subtitle,
        action,
        colors,
        tonalElevation,
        shadowElevation,
        semanticProperties,
        {
            isLocalePickerDialogOpen = true
        },
    )
}
