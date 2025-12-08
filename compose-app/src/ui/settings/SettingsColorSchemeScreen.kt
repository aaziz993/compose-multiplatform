package ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import clib.presentation.theme.model.Theme
import compose_app.generated.resources.Res
import compose_app.generated.resources.background
import compose_app.generated.resources.error
import compose_app.generated.resources.error_container
import compose_app.generated.resources.inverse_on_surface
import compose_app.generated.resources.inverse_primary
import compose_app.generated.resources.inverse_surface
import compose_app.generated.resources.on_background
import compose_app.generated.resources.on_error
import compose_app.generated.resources.on_error_container
import compose_app.generated.resources.on_primary
import compose_app.generated.resources.on_primary_container
import compose_app.generated.resources.on_primary_fixed
import compose_app.generated.resources.on_primary_fixed_variant
import compose_app.generated.resources.on_secondary
import compose_app.generated.resources.on_secondary_container
import compose_app.generated.resources.on_secondary_fixed
import compose_app.generated.resources.on_secondary_fixed_variant
import compose_app.generated.resources.on_surface
import compose_app.generated.resources.on_surface_variant
import compose_app.generated.resources.on_tertiary
import compose_app.generated.resources.on_tertiary_container
import compose_app.generated.resources.on_tertiary_fixed
import compose_app.generated.resources.on_tertiary_fixed_variant
import compose_app.generated.resources.outline
import compose_app.generated.resources.outline_variant
import compose_app.generated.resources.primary
import compose_app.generated.resources.primary_container
import compose_app.generated.resources.primary_fixed
import compose_app.generated.resources.primary_fixed_dim
import compose_app.generated.resources.scrim
import compose_app.generated.resources.secondary
import compose_app.generated.resources.secondary_container
import compose_app.generated.resources.secondary_fixed
import compose_app.generated.resources.secondary_fixed_dim
import compose_app.generated.resources.surface
import compose_app.generated.resources.surface_bright
import compose_app.generated.resources.surface_container
import compose_app.generated.resources.surface_container_high
import compose_app.generated.resources.surface_container_highest
import compose_app.generated.resources.surface_container_low
import compose_app.generated.resources.surface_container_lowest
import compose_app.generated.resources.surface_dim
import compose_app.generated.resources.surface_tint
import compose_app.generated.resources.surface_variant
import compose_app.generated.resources.tertiary
import compose_app.generated.resources.tertiary_container
import compose_app.generated.resources.tertiary_fixed
import compose_app.generated.resources.tertiary_fixed_dim
import org.jetbrains.compose.resources.stringResource
import presentation.components.settings.SettingsColorPickerBottomSheet
import ui.navigation.presentation.SettingsColorScheme

@Composable
public fun SettingsColorSchemeScreen(
    modifier: Modifier = Modifier,
    route: SettingsColorScheme = SettingsColorScheme,
    defaultTheme: Theme = Theme(),
    theme: Theme = defaultTheme,
    onThemeChange: (Theme) -> Unit = {},
): Unit = Column(
    modifier = modifier,
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
) {
    val colorScheme = theme.currentColorScheme
    val copyColorScheme = theme.copyColorScheme()

    SettingsColorPickerBottomSheet(
        stringResource(Res.string.primary),
        colorScheme.primary,
    ) { value ->
        onThemeChange(copyColorScheme(colorScheme.copy(primary = value)))
    }

    SettingsColorPickerBottomSheet(
        stringResource(Res.string.on_primary),
        colorScheme.onPrimary,
    ) { value ->
        onThemeChange(copyColorScheme(colorScheme.copy(onPrimary = value)))
    }

    SettingsColorPickerBottomSheet(
        stringResource(Res.string.primary_container),
        colorScheme.primaryContainer,
    ) { value ->
        onThemeChange(copyColorScheme(colorScheme.copy(primaryContainer = value)))
    }

    SettingsColorPickerBottomSheet(
        stringResource(Res.string.on_primary_container),
        colorScheme.onPrimaryContainer,
    ) { value ->
        onThemeChange(copyColorScheme(colorScheme.copy(onPrimaryContainer = value)))
    }

    SettingsColorPickerBottomSheet(
        stringResource(Res.string.inverse_primary),
        colorScheme.inversePrimary,
    ) { value ->
        onThemeChange(copyColorScheme(colorScheme.copy(inversePrimary = value)))
    }

    SettingsColorPickerBottomSheet(
        stringResource(Res.string.secondary),
        colorScheme.secondary,
    ) { value ->
        onThemeChange(copyColorScheme(colorScheme.copy(secondary = value)))
    }

    SettingsColorPickerBottomSheet(
        stringResource(Res.string.on_secondary),
        colorScheme.onSecondary,
    ) { value ->
        onThemeChange(copyColorScheme(colorScheme.copy(onSecondary = value)))
    }

    SettingsColorPickerBottomSheet(
        stringResource(Res.string.secondary_container),
        colorScheme.secondaryContainer,
    ) { value ->
        onThemeChange(copyColorScheme(colorScheme.copy(secondaryContainer = value)))
    }

    SettingsColorPickerBottomSheet(
        stringResource(Res.string.on_secondary_container),
        colorScheme.onSecondaryContainer,
    ) { value ->
        onThemeChange(copyColorScheme(colorScheme.copy(onSecondaryContainer = value)))
    }

    SettingsColorPickerBottomSheet(
        stringResource(Res.string.tertiary),
        colorScheme.tertiary,
    ) { value ->
        onThemeChange(copyColorScheme(colorScheme.copy(tertiary = value)))
    }

    SettingsColorPickerBottomSheet(
        stringResource(Res.string.on_tertiary),
        colorScheme.onTertiary,
    ) { value ->
        onThemeChange(copyColorScheme(colorScheme.copy(onTertiary = value)))
    }

    SettingsColorPickerBottomSheet(
        stringResource(Res.string.tertiary_container),
        colorScheme.tertiaryContainer,
    ) { value ->
        onThemeChange(copyColorScheme(colorScheme.copy(tertiaryContainer = value)))
    }

    SettingsColorPickerBottomSheet(
        stringResource(Res.string.on_tertiary_container),
        colorScheme.onTertiaryContainer,
    ) { value ->
        onThemeChange(copyColorScheme(colorScheme.copy(onTertiaryContainer = value)))
    }

    SettingsColorPickerBottomSheet(
        stringResource(Res.string.background),
        colorScheme.background,
    ) { value ->
        onThemeChange(copyColorScheme(colorScheme.copy(background = value)))
    }

    SettingsColorPickerBottomSheet(
        stringResource(Res.string.on_background),
        colorScheme.onBackground,
    ) { value ->
        onThemeChange(copyColorScheme(colorScheme.copy(onBackground = value)))
    }

    SettingsColorPickerBottomSheet(
        stringResource(Res.string.surface),
        colorScheme.surface,
    ) { value ->
        onThemeChange(copyColorScheme(colorScheme.copy(surface = value)))
    }

    SettingsColorPickerBottomSheet(
        stringResource(Res.string.on_surface),
        colorScheme.onSurface,
    ) { value ->
        onThemeChange(copyColorScheme(colorScheme.copy(onSurface = value)))
    }

    SettingsColorPickerBottomSheet(
        stringResource(Res.string.surface_variant),
        colorScheme.surfaceVariant,
    ) { value ->
        onThemeChange(copyColorScheme(colorScheme.copy(surfaceVariant = value)))
    }

    SettingsColorPickerBottomSheet(
        stringResource(Res.string.on_surface_variant),
        colorScheme.onSurfaceVariant,
    ) { value ->
        onThemeChange(copyColorScheme(colorScheme.copy(onSurfaceVariant = value)))
    }

    SettingsColorPickerBottomSheet(
        stringResource(Res.string.surface_tint),
        colorScheme.surfaceTint,
    ) { value ->
        onThemeChange(copyColorScheme(colorScheme.copy(surfaceTint = value)))
    }

    SettingsColorPickerBottomSheet(
        stringResource(Res.string.inverse_surface),
        colorScheme.inverseSurface,
    ) { value ->
        onThemeChange(copyColorScheme(colorScheme.copy(inverseSurface = value)))
    }

    SettingsColorPickerBottomSheet(
        stringResource(Res.string.inverse_on_surface),
        colorScheme.inverseOnSurface,
    ) { value ->
        onThemeChange(copyColorScheme(colorScheme.copy(inverseOnSurface = value)))
    }

    SettingsColorPickerBottomSheet(
        stringResource(Res.string.error),
        colorScheme.error,
    ) { value ->
        onThemeChange(copyColorScheme(colorScheme.copy(error = value)))
    }

    SettingsColorPickerBottomSheet(
        stringResource(Res.string.on_error),
        colorScheme.onError,
    ) { value ->
        onThemeChange(copyColorScheme(colorScheme.copy(onError = value)))
    }

    SettingsColorPickerBottomSheet(
        stringResource(Res.string.error_container),
        colorScheme.errorContainer,
    ) { value ->
        onThemeChange(copyColorScheme(colorScheme.copy(errorContainer = value)))
    }

    SettingsColorPickerBottomSheet(
        stringResource(Res.string.on_error_container),
        colorScheme.onErrorContainer,
    ) { value ->
        onThemeChange(copyColorScheme(colorScheme.copy(onErrorContainer = value)))
    }

    SettingsColorPickerBottomSheet(
        stringResource(Res.string.outline),
        colorScheme.outline,
    ) { value ->
        onThemeChange(copyColorScheme(colorScheme.copy(outline = value)))
    }

    SettingsColorPickerBottomSheet(
        stringResource(Res.string.outline_variant),
        colorScheme.outlineVariant,
    ) { value ->
        onThemeChange(copyColorScheme(colorScheme.copy(outlineVariant = value)))
    }

    SettingsColorPickerBottomSheet(
        stringResource(Res.string.scrim),
        colorScheme.scrim,
    ) { value ->
        onThemeChange(copyColorScheme(colorScheme.copy(scrim = value)))
    }

    SettingsColorPickerBottomSheet(
        stringResource(Res.string.surface_bright),
        colorScheme.surfaceBright,
    ) { value ->
        onThemeChange(copyColorScheme(colorScheme.copy(surfaceBright = value)))
    }

    SettingsColorPickerBottomSheet(
        stringResource(Res.string.surface_dim),
        colorScheme.surfaceDim,
    ) { value ->
        onThemeChange(copyColorScheme(colorScheme.copy(surfaceDim = value)))
    }

    SettingsColorPickerBottomSheet(
        stringResource(Res.string.surface_container),
        colorScheme.surfaceContainer,
    ) { value ->
        onThemeChange(copyColorScheme(colorScheme.copy(surfaceContainer = value)))
    }

    SettingsColorPickerBottomSheet(
        stringResource(Res.string.surface_container_high),
        colorScheme.surfaceContainerHigh,
    ) { value ->
        onThemeChange(copyColorScheme(colorScheme.copy(surfaceContainerHigh = value)))
    }

    SettingsColorPickerBottomSheet(
        stringResource(Res.string.surface_container_highest),
        colorScheme.surfaceContainerHighest,
    ) { value ->
        onThemeChange(copyColorScheme(colorScheme.copy(surfaceContainerHighest = value)))
    }

    SettingsColorPickerBottomSheet(
        stringResource(Res.string.surface_container_low),
        colorScheme.surfaceContainerLow,
    ) { value ->
        onThemeChange(copyColorScheme(colorScheme.copy(surfaceContainerLow = value)))
    }

    SettingsColorPickerBottomSheet(
        stringResource(Res.string.surface_container_lowest),
        colorScheme.surfaceContainerLowest,
    ) { value ->
        onThemeChange(copyColorScheme(colorScheme.copy(surfaceContainerLowest = value)))
    }

    SettingsColorPickerBottomSheet(
        stringResource(Res.string.primary_fixed),
        colorScheme.primaryFixed,
    ) { value ->
        onThemeChange(copyColorScheme(colorScheme.copy(primaryFixed = value)))
    }

    SettingsColorPickerBottomSheet(
        stringResource(Res.string.primary_fixed_dim),
        colorScheme.primaryFixedDim,
    ) { value ->
        onThemeChange(copyColorScheme(colorScheme.copy(primaryFixedDim = value)))
    }

    SettingsColorPickerBottomSheet(
        stringResource(Res.string.on_primary_fixed),
        colorScheme.onPrimaryFixed,
    ) { value ->
        onThemeChange(copyColorScheme(colorScheme.copy(onPrimaryFixed = value)))
    }

    SettingsColorPickerBottomSheet(
        stringResource(Res.string.on_primary_fixed_variant),
        colorScheme.onPrimaryFixedVariant,
    ) { value ->
        onThemeChange(copyColorScheme(colorScheme.copy(onPrimaryFixedVariant = value)))
    }

    SettingsColorPickerBottomSheet(
        stringResource(Res.string.secondary_fixed),
        colorScheme.secondaryFixed,
    ) { value ->
        onThemeChange(copyColorScheme(colorScheme.copy(secondaryFixed = value)))
    }

    SettingsColorPickerBottomSheet(
        stringResource(Res.string.secondary_fixed_dim),
        colorScheme.secondaryFixedDim,
    ) { value ->
        onThemeChange(copyColorScheme(colorScheme.copy(secondaryFixedDim = value)))
    }

    SettingsColorPickerBottomSheet(
        stringResource(Res.string.on_secondary_fixed),
        colorScheme.onSecondaryFixed,
    ) { value ->
        onThemeChange(copyColorScheme(colorScheme.copy(onSecondaryFixed = value)))
    }

    SettingsColorPickerBottomSheet(
        stringResource(Res.string.on_secondary_fixed_variant),
        colorScheme.onSecondaryFixedVariant,
    ) { value ->
        onThemeChange(copyColorScheme(colorScheme.copy(onSecondaryFixedVariant = value)))
    }

    SettingsColorPickerBottomSheet(
        stringResource(Res.string.tertiary_fixed),
        colorScheme.tertiaryFixed,
    ) { value ->
        onThemeChange(copyColorScheme(colorScheme.copy(tertiaryFixed = value)))
    }

    SettingsColorPickerBottomSheet(
        stringResource(Res.string.tertiary_fixed_dim),
        colorScheme.tertiaryFixedDim,
    ) { value ->
        onThemeChange(copyColorScheme(colorScheme.copy(tertiaryFixedDim = value)))
    }

    SettingsColorPickerBottomSheet(
        stringResource(Res.string.on_tertiary_fixed),
        colorScheme.onTertiaryFixed,
    ) { value ->
        onThemeChange(copyColorScheme(colorScheme.copy(onTertiaryFixed = value)))
    }

    SettingsColorPickerBottomSheet(
        stringResource(Res.string.on_tertiary_fixed_variant),
        colorScheme.onTertiaryFixedVariant,
    ) { value ->
        onThemeChange(copyColorScheme(colorScheme.copy(onTertiaryFixedVariant = value)))
    }
}

@Preview
@Composable
private fun PreviewSettingsColorSchemeScreen(): Unit = SettingsColorSchemeScreen()
