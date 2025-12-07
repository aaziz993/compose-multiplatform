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
    val colorScheme = theme.currentColorScheme
    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_primary),
        colorScheme.primary,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(primary = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_on_primary),
        colorScheme.onPrimary,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(onPrimary = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_primary_container),
        colorScheme.primaryContainer,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(primaryContainer = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_on_primary_container),
        colorScheme.onPrimaryContainer,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(onPrimaryContainer = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_inverse_primary),
        colorScheme.inversePrimary,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(inversePrimary = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_secondary),
        colorScheme.secondary,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(secondary = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_on_secondary),
        colorScheme.onSecondary,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(onSecondary = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_secondary_container),
        colorScheme.secondaryContainer,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(secondaryContainer = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_on_secondary_container),
        colorScheme.onSecondaryContainer,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(onSecondaryContainer = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_tertiary),
        colorScheme.tertiary,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(tertiary = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_on_tertiary),
        colorScheme.onTertiary,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(onTertiary = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_tertiary_container),
        colorScheme.tertiaryContainer,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(tertiaryContainer = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_on_tertiary_container),
        colorScheme.onTertiaryContainer,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(onTertiaryContainer = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_background),
        colorScheme.background,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(background = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_on_background),
        colorScheme.onBackground,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(onBackground = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_surface),
        colorScheme.surface,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(surface = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_on_surface),
        colorScheme.onSurface,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(onSurface = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_surface_variant),
        colorScheme.surfaceVariant,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(surfaceVariant = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_on_surface_variant),
        colorScheme.onSurfaceVariant,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(onSurfaceVariant = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_surface_tint),
        colorScheme.surfaceTint,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(surfaceTint = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_inverse_surface),
        colorScheme.inverseSurface,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(inverseSurface = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_inverse_on_surface),
        colorScheme.inverseOnSurface,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(inverseOnSurface = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_error),
        colorScheme.error,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(error = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_on_error),
        colorScheme.onError,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(onError = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_error_container),
        colorScheme.errorContainer,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(errorContainer = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_on_error_container),
        colorScheme.onErrorContainer,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(onErrorContainer = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_outline),
        colorScheme.outline,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(outline = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_outline_variant),
        colorScheme.outlineVariant,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(outlineVariant = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_scrim),
        colorScheme.scrim,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(scrim = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_surface_bright),
        colorScheme.surfaceBright,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(surfaceBright = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_surface_dim),
        colorScheme.surfaceDim,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(surfaceDim = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_surface_container),
        colorScheme.surfaceContainer,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(surfaceContainer = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_surface_container_high),
        colorScheme.surfaceContainerHigh,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(surfaceContainerHigh = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_surface_container_highest),
        colorScheme.surfaceContainerHighest,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(surfaceContainerHighest = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_surface_container_low),
        colorScheme.surfaceContainerLow,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(surfaceContainerLow = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_surface_container_lowest),
        colorScheme.surfaceContainerLowest,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(surfaceContainerLowest = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_primary_fixed),
        colorScheme.primaryFixed,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(primaryFixed = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_primary_fixed_dim),
        colorScheme.primaryFixedDim,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(primaryFixedDim = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_on_primary_fixed),
        colorScheme.onPrimaryFixed,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(onPrimaryFixed = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_on_primary_fixed_variant),
        colorScheme.onPrimaryFixedVariant,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(onPrimaryFixedVariant = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_secondary_fixed),
        colorScheme.secondaryFixed,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(secondaryFixed = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_secondary_fixed_dim),
        colorScheme.secondaryFixedDim,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(secondaryFixedDim = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_on_secondary_fixed),
        colorScheme.onSecondaryFixed,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(onSecondaryFixed = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_on_secondary_fixed_variant),
        colorScheme.onSecondaryFixedVariant,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(onSecondaryFixedVariant = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_tertiary_fixed),
        colorScheme.tertiaryFixed,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(tertiaryFixed = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_tertiary_fixed_dim),
        colorScheme.tertiaryFixedDim,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(tertiaryFixedDim = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_on_tertiary_fixed),
        colorScheme.onTertiaryFixed,
    ) { value ->
        onThemeChange(theme.copyColorScheme { colorScheme -> colorScheme.copy(onTertiaryFixed = value) })
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_on_tertiary_fixed_variant),
        colorScheme.onTertiaryFixedVariant,
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
