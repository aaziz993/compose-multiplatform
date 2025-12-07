package presentation.components.settings

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import clib.presentation.components.model.item.Item
import clib.presentation.components.picker.model.Picker
import clib.presentation.components.settings.SettingsListPickerDialog
import compose_app.generated.resources.Res
import compose_app.generated.resources.clear
import compose_app.generated.resources.platform
import compose_app.generated.resources.search
import data.type.primitives.asStringResource
import org.jetbrains.compose.resources.stringResource

@Composable
public fun SettingsListPickerDialog(

): Unit =
    SettingsListPickerDialog(
        title = { Text(text = stringResource(Res.string.platform)) },
        values = platforms,
        icon = { Icon(Icons.Default.Devices, theme.currentDynamicColorScheme.platform.name) },
        subtitle = { Text(theme.currentDynamicColorScheme.platform.name) },
        modifier = Modifier,
        enabled = true,
        item = { value ->
            Item(
                text = { Text(value.asStringResource()) },
                icon = { Icon(Icons.Default.Devices, value.asStringResource()) },
            )
        },
        picker = Picker(
            headerTitle = stringResource(Res.string.platform),
            searchHint = stringResource(Res.string.search),
            clear = stringResource(Res.string.clear),
        ),
    ) { value ->
        onThemeChange(theme.copyDynamicColorScheme { colorScheme -> colorScheme.copy(platform = value) })
        false
    }
