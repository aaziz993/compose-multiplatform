package ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Animation
import androidx.compose.material.icons.filled.Contrast
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.SmartDisplay
import androidx.compose.material.icons.outlined.Animation
import androidx.compose.material.icons.outlined.SmartDisplay
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import clib.presentation.components.model.item.Item
import clib.presentation.components.picker.model.Picker
import clib.presentation.components.settings.SettingsListPickerDialog
import clib.presentation.components.settings.SettingsSlider
import clib.presentation.theme.model.Theme
import com.materialkolor.scheme.DynamicScheme
import compose_app.generated.resources.Res
import compose_app.generated.resources.amoled
import compose_app.generated.resources.animate
import compose_app.generated.resources.clear
import compose_app.generated.resources.color_scheme_seed_color
import compose_app.generated.resources.contrast
import compose_app.generated.resources.error
import compose_app.generated.resources.locale
import compose_app.generated.resources.neutral
import compose_app.generated.resources.neutral_variant
import compose_app.generated.resources.platform
import compose_app.generated.resources.primary
import compose_app.generated.resources.search
import compose_app.generated.resources.secondary
import compose_app.generated.resources.tertiary
import data.type.primitives.asStringResource
import data.type.primitives.string.asStringResource
import org.jetbrains.compose.resources.stringResource
import presentation.components.settings.SettingsSwitch
import ui.navigation.presentation.SettingsDynamicColorScheme

@Composable
public fun SettingsDynamicColorSchemeScreen(
    modifier: Modifier = Modifier,
    route: SettingsDynamicColorScheme = SettingsDynamicColorScheme,
    defaultTheme: Theme = Theme(),
    theme: Theme = defaultTheme,
    onThemeChange: (Theme) -> Unit = {},
): Unit = Column(
    modifier = modifier,
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
) {
    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_seed_color),
        theme.currentDynamicColorScheme.seedColor,
    ) { value ->
        onThemeChange(theme.copyDynamicColorScheme { colorScheme -> colorScheme.copy(seedColor = value) })
    }

    SettingsSwitch(
        title = stringResource(Res.string.amoled),
        value = theme.currentDynamicColorScheme.isAmoled,
        trueIcon = Icons.Outlined.SmartDisplay,
        falseIcon = Icons.Filled.SmartDisplay,
        onCheckedChange = { value ->
            onThemeChange(theme.copyDynamicColorScheme { colorScheme -> colorScheme.copy(isAmoled = value) })
        },
    )

    theme.currentDynamicColorScheme.primary?.let { primary ->
        SettingsColorSchemeColor(
            stringResource(Res.string.primary),
            primary,
        ) { value ->
            onThemeChange(theme.copyDynamicColorScheme { colorScheme -> colorScheme.copy(primary = value) })
        }
    }

    theme.currentDynamicColorScheme.secondary?.let { secondary ->
        SettingsColorSchemeColor(
            stringResource(Res.string.secondary),
            secondary,
        ) { value ->
            onThemeChange(theme.copyDynamicColorScheme { colorScheme -> colorScheme.copy(secondary = value) })
        }
    }

    theme.currentDynamicColorScheme.tertiary?.let { tertiary ->
        SettingsColorSchemeColor(
            stringResource(Res.string.tertiary),
            tertiary,
        ) { value ->
            onThemeChange(theme.copyDynamicColorScheme { colorScheme -> colorScheme.copy(tertiary = value) })
        }
    }

    theme.currentDynamicColorScheme.neutral?.let { neutral ->
        SettingsColorSchemeColor(
            stringResource(Res.string.neutral),
            neutral,
        ) { value ->
            onThemeChange(theme.copyDynamicColorScheme { colorScheme -> colorScheme.copy(neutral = value) })
        }
    }

    theme.currentDynamicColorScheme.neutralVariant?.let { neutralVariant ->
        SettingsColorSchemeColor(
            stringResource(Res.string.neutral_variant),
            neutralVariant,
        ) { value ->
            onThemeChange(theme.copyDynamicColorScheme { colorScheme -> colorScheme.copy(neutralVariant = value) })
        }
    }

    theme.currentDynamicColorScheme.error?.let { error ->
        SettingsColorSchemeColor(
            stringResource(Res.string.error),
            error,
        ) { value ->
            onThemeChange(theme.copyDynamicColorScheme { colorScheme -> colorScheme.copy(error = value) })
        }
    }

    SettingsSlider(
        title = { Text(stringResource(Res.string.contrast)) },
        value = theme.currentDynamicColorScheme.contrastLevel.toFloat(),
        icon = { Icon(Icons.Default.Contrast, "") },
        enabled = true,
        valueRange = -1f..1f,
    ) { value, _ ->
        onThemeChange(theme.copyDynamicColorScheme { colorScheme -> colorScheme.copy(contrastLevel = value.toDouble()) })
    }

    val platforms = DynamicScheme.Platform.entries.toList()

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

    SettingsSwitch(
        title = stringResource(Res.string.animate),
        value = theme.currentDynamicColorScheme.animate,
        trueIcon = Icons.Outlined.Animation,
        falseIcon = Icons.Filled.Animation,
        onCheckedChange = { value ->
            onThemeChange(theme.copyDynamicColorScheme { colorScheme -> colorScheme.copy(animate = value) })
        },
    )

//    val animationSpec: AnimationSpecSerial<ColorSerial>? = null,
}

@Preview
@Composable
private fun PreviewSettingsDynamicColorSchemeScreen(): Unit = SettingsDynamicColorSchemeScreen()
