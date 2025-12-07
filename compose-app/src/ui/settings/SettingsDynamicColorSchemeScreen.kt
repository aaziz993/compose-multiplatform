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
import presentation.components.settings.SettingsListPickerDialog
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

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_seed_color),
        colorScheme.seedColor,
    ) { value ->
        onThemeChange(theme.copyDynamicColorScheme { colorScheme -> colorScheme.copy(seedColor = value) })
    }

    SettingsSwitch(
        title = stringResource(Res.string.amoled),
        value = colorScheme.isAmoled,
        trueIcon = Icons.Outlined.SmartDisplay,
        falseIcon = Icons.Filled.SmartDisplay,
        onCheckedChange = { value ->
            onThemeChange(theme.copyDynamicColorScheme { colorScheme -> colorScheme.copy(isAmoled = value) })
        },
    )

    colorScheme.primary?.let { primary ->
        SettingsColorSchemeColor(
            stringResource(Res.string.primary),
            primary,
        ) { value ->
            onThemeChange(theme.copyDynamicColorScheme { colorScheme -> colorScheme.copy(primary = value) })
        }
    }

    colorScheme.secondary?.let { secondary ->
        SettingsColorSchemeColor(
            stringResource(Res.string.secondary),
            secondary,
        ) { value ->
            onThemeChange(theme.copyDynamicColorScheme { colorScheme -> colorScheme.copy(secondary = value) })
        }
    }

    colorScheme.tertiary?.let { tertiary ->
        SettingsColorSchemeColor(
            stringResource(Res.string.tertiary),
            tertiary,
        ) { value ->
            onThemeChange(theme.copyDynamicColorScheme { colorScheme -> colorScheme.copy(tertiary = value) })
        }
    }

    colorScheme.neutral?.let { neutral ->
        SettingsColorSchemeColor(
            stringResource(Res.string.neutral),
            neutral,
        ) { value ->
            onThemeChange(theme.copyDynamicColorScheme { colorScheme -> colorScheme.copy(neutral = value) })
        }
    }

    colorScheme.neutralVariant?.let { neutralVariant ->
        SettingsColorSchemeColor(
            stringResource(Res.string.neutral_variant),
            neutralVariant,
        ) { value ->
            onThemeChange(theme.copyDynamicColorScheme { colorScheme -> colorScheme.copy(neutralVariant = value) })
        }
    }

    colorScheme.error?.let { error ->
        SettingsColorSchemeColor(
            stringResource(Res.string.error),
            error,
        ) { value ->
            onThemeChange(theme.copyDynamicColorScheme { colorScheme -> colorScheme.copy(error = value) })
        }
    }

    SettingsSlider(
        title = { Text(stringResource(Res.string.contrast)) },
        value = colorScheme.contrastLevel.toFloat(),
        icon = { Icon(Icons.Default.Contrast, "") },
        enabled = true,
        valueRange = -1f..1f,
    ) { value, _ ->
        onThemeChange(theme.copyDynamicColorScheme { colorScheme -> colorScheme.copy(contrastLevel = value.toDouble()) })
    }

    val platforms = DynamicScheme.Platform.entries.toList()

    SettingsListPickerDialog(
        colorScheme.platform,
        values = platforms,
        title = stringResource(Res.string.platform),
        icon = Icons.Default.Devices,
        modifier = Modifier,
        enabled = true,
    ) { value ->
        onThemeChange(theme.copyDynamicColorScheme { colorScheme -> colorScheme.copy(platform = value) })
        false
    }

    SettingsSwitch(
        title = stringResource(Res.string.animate),
        value = colorScheme.animate,
        trueIcon = Icons.Outlined.Animation,
        falseIcon = Icons.Filled.Animation,
        onCheckedChange = { value ->
            onThemeChange(theme.copyDynamicColorScheme { colorScheme -> colorScheme.copy(animate = value) })
        },
    )

    colorScheme.animationSpec?.let { animationSpec ->
//        SettingsListPickerDialog(
//            animationSpec,
//            values = platforms,
//            title = stringResource(Res.string.platform),
//            icon = Icons.Default.Devices,
//            modifier = Modifier,
//            enabled = true,
//        ) { value ->
//            onThemeChange(theme.copyDynamicColorScheme { colorScheme -> colorScheme.copy(platform = value) })
//            false
//        }
    }

//    val animationSpec: AnimationSpecSerial<ColorSerial>? = null,
}

@Preview
@Composable
private fun PreviewSettingsDynamicColorSchemeScreen(): Unit = SettingsDynamicColorSchemeScreen()
