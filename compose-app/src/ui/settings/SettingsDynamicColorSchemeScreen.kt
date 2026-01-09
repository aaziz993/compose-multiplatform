package ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import clib.data.type.primitives.string.stringResource
import clib.presentation.theme.model.Theme
import compose_app.generated.resources.Res
import compose_app.generated.resources.error
import compose_app.generated.resources.neutral
import compose_app.generated.resources.neutral_variant
import compose_app.generated.resources.primary
import compose_app.generated.resources.secondary
import compose_app.generated.resources.seed_color
import compose_app.generated.resources.tertiary
import presentation.components.settings.SettingsColorPickerBottomSheet
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
    verticalArrangement = Arrangement.Top,
    horizontalAlignment = Alignment.CenterHorizontally,
) {
    SettingsColorPickerBottomSheet(
        stringResource(Res.string.seed_color),
        theme.dynamicColorScheme.seedColor,
    ) { value ->
        onThemeChange(theme.copy(dynamicColorScheme = theme.dynamicColorScheme.copy(seedColor = value)))
        false
    }

    theme.dynamicColorScheme.primary?.let { primary ->
        SettingsColorPickerBottomSheet(
            stringResource(Res.string.primary),
            primary,
        ) { value ->
            onThemeChange(theme.copy(dynamicColorScheme = theme.dynamicColorScheme.copy(primary = value)))
            false
        }
    }

    theme.dynamicColorScheme.secondary?.let { secondary ->
        SettingsColorPickerBottomSheet(
            stringResource(Res.string.secondary),
            secondary,
        ) { value ->
            onThemeChange(theme.copy(dynamicColorScheme = theme.dynamicColorScheme.copy(secondary = value)))
            false
        }
    }

    theme.dynamicColorScheme.tertiary?.let { tertiary ->
        SettingsColorPickerBottomSheet(
            stringResource(Res.string.tertiary),
            tertiary,
        ) { value ->
            onThemeChange(theme.copy(dynamicColorScheme = theme.dynamicColorScheme.copy(tertiary = value)))
            false
        }
    }

    theme.dynamicColorScheme.neutral?.let { neutral ->
        SettingsColorPickerBottomSheet(
            stringResource(Res.string.neutral),
            neutral,
        ) { value ->
            onThemeChange(theme.copy(dynamicColorScheme = theme.dynamicColorScheme.copy(neutral = value)))
            false
        }
    }

    theme.dynamicColorScheme.neutralVariant?.let { neutralVariant ->
        SettingsColorPickerBottomSheet(
            stringResource(Res.string.neutral_variant),
            neutralVariant,
        ) { value ->
            onThemeChange(theme.copy(dynamicColorScheme = theme.dynamicColorScheme.copy(neutralVariant = value)))
            false
        }
    }

    theme.dynamicColorScheme.error?.let { error ->
        SettingsColorPickerBottomSheet(
            stringResource(Res.string.error),
            error,
        ) { value ->
            onThemeChange(theme.copy(dynamicColorScheme = theme.dynamicColorScheme.copy(error = value)))
            false
        }
    }
}

@Preview
@Composable
private fun PreviewSettingsDynamicColorSchemeScreen(): Unit = SettingsDynamicColorSchemeScreen()
