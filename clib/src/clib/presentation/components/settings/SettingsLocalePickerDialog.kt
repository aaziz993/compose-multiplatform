package clib.presentation.components.settings

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import clib.presentation.components.country.LocalePickerDialog
import clib.presentation.components.country.model.CountryPicker
import com.alorma.compose.settings.ui.SettingsMenuLink
import com.alorma.compose.settings.ui.base.internal.LocalSettingsGroupEnabled
import com.alorma.compose.settings.ui.base.internal.SettingsTileColors
import com.alorma.compose.settings.ui.base.internal.SettingsTileDefaults
import klib.data.location.country.Country
import klib.data.location.locale.Locale

@Composable
public fun SettingsLocalePickerDialog(
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
    country: @Composable (Locale) -> Country = { locale -> locale.country()!! },
    textStyle: TextStyle = TextStyle(),
    itemPadding: Int = 10,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    picker: CountryPicker = CountryPicker(),
    onItemClicked: (Locale) -> Boolean,
) {
    var localePickerDialog by remember { mutableStateOf(false) }
    if (localePickerDialog)
        LocalePickerDialog(
            { value ->
                localePickerDialog = onItemClicked(value)
            },
            {
                localePickerDialog = false
            },
            Modifier.padding(end = 16.dp),
            locales,
            country,
            textStyle,
            itemPadding,
            backgroundColor,
            picker,
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
        localePickerDialog = true
    }
}
