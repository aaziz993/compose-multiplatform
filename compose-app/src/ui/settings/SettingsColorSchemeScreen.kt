package ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import clib.presentation.theme.model.Theme
import compose_app.generated.resources.Res
import compose_app.generated.resources.color_scheme_background
import compose_app.generated.resources.color_scheme_error
import compose_app.generated.resources.color_scheme_error_container
import compose_app.generated.resources.color_scheme_inverse_on_surface
import compose_app.generated.resources.color_scheme_inverse_primary
import compose_app.generated.resources.color_scheme_inverse_surface
import compose_app.generated.resources.color_scheme_on_background
import compose_app.generated.resources.color_scheme_on_error
import compose_app.generated.resources.color_scheme_on_error_container
import compose_app.generated.resources.color_scheme_on_primary
import compose_app.generated.resources.color_scheme_on_primary_container
import compose_app.generated.resources.color_scheme_on_primary_fixed
import compose_app.generated.resources.color_scheme_on_primary_fixed_variant
import compose_app.generated.resources.color_scheme_on_secondary
import compose_app.generated.resources.color_scheme_on_secondary_container
import compose_app.generated.resources.color_scheme_on_secondary_fixed
import compose_app.generated.resources.color_scheme_on_secondary_fixed_variant
import compose_app.generated.resources.color_scheme_on_surface
import compose_app.generated.resources.color_scheme_on_surface_variant
import compose_app.generated.resources.color_scheme_on_tertiary
import compose_app.generated.resources.color_scheme_on_tertiary_container
import compose_app.generated.resources.color_scheme_on_tertiary_fixed
import compose_app.generated.resources.color_scheme_on_tertiary_fixed_variant
import compose_app.generated.resources.color_scheme_outline
import compose_app.generated.resources.color_scheme_outline_variant
import compose_app.generated.resources.color_scheme_primary
import compose_app.generated.resources.color_scheme_primary_container
import compose_app.generated.resources.color_scheme_primary_fixed
import compose_app.generated.resources.color_scheme_primary_fixed_dim
import compose_app.generated.resources.color_scheme_scrim
import compose_app.generated.resources.color_scheme_secondary
import compose_app.generated.resources.color_scheme_secondary_container
import compose_app.generated.resources.color_scheme_secondary_fixed
import compose_app.generated.resources.color_scheme_secondary_fixed_dim
import compose_app.generated.resources.color_scheme_surface
import compose_app.generated.resources.color_scheme_surface_bright
import compose_app.generated.resources.color_scheme_surface_container
import compose_app.generated.resources.color_scheme_surface_container_high
import compose_app.generated.resources.color_scheme_surface_container_highest
import compose_app.generated.resources.color_scheme_surface_container_low
import compose_app.generated.resources.color_scheme_surface_container_lowest
import compose_app.generated.resources.color_scheme_surface_dim
import compose_app.generated.resources.color_scheme_surface_tint
import compose_app.generated.resources.color_scheme_surface_variant
import compose_app.generated.resources.color_scheme_tertiary
import compose_app.generated.resources.color_scheme_tertiary_container
import compose_app.generated.resources.color_scheme_tertiary_fixed
import compose_app.generated.resources.color_scheme_tertiary_fixed_dim
import org.jetbrains.compose.resources.stringResource
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
    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_primary),
        theme.currentColorScheme.primary,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(primary = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_on_primary),
        theme.currentColorScheme.onPrimary,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(onPrimary = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_primary_container),
        theme.currentColorScheme.primaryContainer,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(primaryContainer = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_on_primary_container),
        theme.currentColorScheme.onPrimaryContainer,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(onPrimaryContainer = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_inverse_primary),
        theme.currentColorScheme.inversePrimary,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(inversePrimary = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_secondary),
        theme.currentColorScheme.secondary,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(secondary = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_on_secondary),
        theme.currentColorScheme.onSecondary,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(onSecondary = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_secondary_container),
        theme.currentColorScheme.secondaryContainer,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(secondaryContainer = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_on_secondary_container),
        theme.currentColorScheme.onSecondaryContainer,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(onSecondaryContainer = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_tertiary),
        theme.currentColorScheme.tertiary,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(tertiary = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_on_tertiary),
        theme.currentColorScheme.onTertiary,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(onTertiary = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_tertiary_container),
        theme.currentColorScheme.tertiaryContainer,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(tertiaryContainer = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_on_tertiary_container),
        theme.currentColorScheme.onTertiaryContainer,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(onTertiaryContainer = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_background),
        theme.currentColorScheme.background,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(background = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_on_background),
        theme.currentColorScheme.onBackground,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(onBackground = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_surface),
        theme.currentColorScheme.surface,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(surface = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_on_surface),
        theme.currentColorScheme.onSurface,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(onSurface = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_surface_variant),
        theme.currentColorScheme.surfaceVariant,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(surfaceVariant = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_on_surface_variant),
        theme.currentColorScheme.onSurfaceVariant,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(onSurfaceVariant = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_surface_tint),
        theme.currentColorScheme.surfaceTint,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(surfaceTint = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_inverse_surface),
        theme.currentColorScheme.inverseSurface,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(inverseSurface = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_inverse_on_surface),
        theme.currentColorScheme.inverseOnSurface,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(inverseOnSurface = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_error),
        theme.currentColorScheme.error,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(error = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_on_error),
        theme.currentColorScheme.onError,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(onError = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_error_container),
        theme.currentColorScheme.errorContainer,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(errorContainer = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_on_error_container),
        theme.currentColorScheme.onErrorContainer,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(onErrorContainer = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_outline),
        theme.currentColorScheme.outline,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(outline = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_outline_variant),
        theme.currentColorScheme.outlineVariant,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(outlineVariant = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_scrim),
        theme.currentColorScheme.scrim,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(scrim = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_surface_bright),
        theme.currentColorScheme.surfaceBright,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(surfaceBright = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_surface_dim),
        theme.currentColorScheme.surfaceDim,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(surfaceDim = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_surface_container),
        theme.currentColorScheme.surfaceContainer,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(surfaceContainer = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_surface_container_high),
        theme.currentColorScheme.surfaceContainerHigh,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(surfaceContainerHigh = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_surface_container_highest),
        theme.currentColorScheme.surfaceContainerHighest,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(surfaceContainerHighest = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_surface_container_low),
        theme.currentColorScheme.surfaceContainerLow,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(surfaceContainerLow = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_surface_container_lowest),
        theme.currentColorScheme.surfaceContainerLowest,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(surfaceContainerLowest = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_primary_fixed),
        theme.currentColorScheme.primaryFixed,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(primaryFixed = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_primary_fixed_dim),
        theme.currentColorScheme.primaryFixedDim,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(primaryFixedDim = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_on_primary_fixed),
        theme.currentColorScheme.onPrimaryFixed,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(onPrimaryFixed = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_on_primary_fixed_variant),
        theme.currentColorScheme.onPrimaryFixedVariant,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(onPrimaryFixedVariant = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_secondary_fixed),
        theme.currentColorScheme.secondaryFixed,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(secondaryFixed = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_secondary_fixed_dim),
        theme.currentColorScheme.secondaryFixedDim,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(secondaryFixedDim = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_on_secondary_fixed),
        theme.currentColorScheme.onSecondaryFixed,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(onSecondaryFixed = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_on_secondary_fixed_variant),
        theme.currentColorScheme.onSecondaryFixedVariant,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(onSecondaryFixedVariant = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_tertiary_fixed),
        theme.currentColorScheme.tertiaryFixed,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(tertiaryFixed = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_tertiary_fixed_dim),
        theme.currentColorScheme.tertiaryFixedDim,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(tertiaryFixedDim = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_on_tertiary_fixed),
        theme.currentColorScheme.onTertiaryFixed,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(onTertiaryFixed = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_on_tertiary_fixed_variant),
        theme.currentColorScheme.onTertiaryFixedVariant,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(onTertiaryFixedVariant = value) })
    }
}

private fun Theme.copyColorScheme(block: (ColorScheme) -> ColorScheme): Theme {
    return this
}

@Preview
@Composable
private fun PreviewSettingsColorSchemeScreen(): Unit = SettingsColorSchemeScreen()
