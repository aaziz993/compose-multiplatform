package ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Contrast
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Monitor
import androidx.compose.material.icons.outlined.Monitor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import clib.data.type.primitives.string.stringResource
import clib.presentation.theme.model.Theme
import com.materialkolor.scheme.DynamicScheme
import compose_app.generated.resources.Res
import compose_app.generated.resources.amoled
import compose_app.generated.resources.contrast
import compose_app.generated.resources.error
import compose_app.generated.resources.neutral
import compose_app.generated.resources.neutral_variant
import compose_app.generated.resources.platform
import compose_app.generated.resources.primary
import compose_app.generated.resources.secondary
import compose_app.generated.resources.seed_color
import compose_app.generated.resources.tertiary
import presentation.components.settings.SettingsColorPickerBottomSheet
import presentation.components.settings.SettingsListPickerDialog
import presentation.components.settings.SettingsSlider
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
    val colorScheme = theme.currentDynamicColorScheme

    SettingsColorPickerBottomSheet(
        stringResource(Res.string.seed_color),
        colorScheme.seedColor,
    ) { value ->
        onThemeChange(theme.copyDynamicColorScheme(colorScheme.copy(seedColor = value)))
        false
    }

    SettingsSwitch(
        title = stringResource(Res.string.amoled),
        value = colorScheme.isAmoled,
        trueIcon = Icons.Filled.Monitor,
        falseIcon = Icons.Outlined.Monitor,
        onCheckedChange = { value ->
            onThemeChange(theme.copyDynamicColorScheme(colorScheme.copy(isAmoled = value)))
            false
        },
    )

    colorScheme.primary?.let { primary ->
        SettingsColorPickerBottomSheet(
            stringResource(Res.string.primary),
            primary,
        ) { value ->
            onThemeChange(theme.copyDynamicColorScheme(colorScheme.copy(primary = value)))
            false
        }
    }

    colorScheme.secondary?.let { secondary ->
        SettingsColorPickerBottomSheet(
            stringResource(Res.string.secondary),
            secondary,
        ) { value ->
            onThemeChange(theme.copyDynamicColorScheme(colorScheme.copy(secondary = value)))
            false
        }
    }

    colorScheme.tertiary?.let { tertiary ->
        SettingsColorPickerBottomSheet(
            stringResource(Res.string.tertiary),
            tertiary,
        ) { value ->
            onThemeChange(theme.copyDynamicColorScheme(colorScheme.copy(tertiary = value)))
            false
        }
    }

    colorScheme.neutral?.let { neutral ->
        SettingsColorPickerBottomSheet(
            stringResource(Res.string.neutral),
            neutral,
        ) { value ->
            onThemeChange(theme.copyDynamicColorScheme(colorScheme.copy(neutral = value)))
            false
        }
    }

    colorScheme.neutralVariant?.let { neutralVariant ->
        SettingsColorPickerBottomSheet(
            stringResource(Res.string.neutral_variant),
            neutralVariant,
        ) { value ->
            onThemeChange(theme.copyDynamicColorScheme(colorScheme.copy(neutralVariant = value)))
            false
        }
    }

    colorScheme.error?.let { error ->
        SettingsColorPickerBottomSheet(
            stringResource(Res.string.error),
            error,
        ) { value ->
            onThemeChange(theme.copyDynamicColorScheme(colorScheme.copy(error = value)))
            false
        }
    }

    SettingsSlider(
        title = stringResource(Res.string.contrast),
        value = colorScheme.contrastLevel.toFloat(),
        icon = Icons.Default.Contrast,
        enabled = true,
        valueRange = -1f..1f,
    ) { value, _ ->
        onThemeChange(theme.copyDynamicColorScheme(colorScheme.copy(contrastLevel = value.toDouble())))
    }

    val platforms = remember { DynamicScheme.Platform.entries.toList() }

    SettingsListPickerDialog(
        colorScheme.platform,
        values = platforms,
        title = stringResource(Res.string.platform),
        icon = Icons.Default.Devices,
        modifier = Modifier,
        enabled = true,
    ) { value ->
        onThemeChange(theme.copyDynamicColorScheme(colorScheme.copy(platform = value)))
        false
        false
    }
}

@Preview
@Composable
private fun PreviewSettingsDynamicColorSchemeScreen(): Unit = SettingsDynamicColorSchemeScreen()
