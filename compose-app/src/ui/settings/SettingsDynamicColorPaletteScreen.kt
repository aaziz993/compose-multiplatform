package ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import clib.data.type.ColorSerial
import clib.presentation.theme.model.AnimationSpecSerial
import clib.presentation.theme.model.Theme
import com.materialkolor.Contrast
import com.materialkolor.scheme.DynamicScheme
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
import ui.navigation.presentation.SettingsColorPalette

@Composable
public fun SettingsDynamicColorPaletteScreen(
    modifier: Modifier = Modifier,
    route: SettingsColorPalette = SettingsColorPalette,
    defaultTheme: Theme = Theme(),
    theme: Theme = defaultTheme,
    onThemeChange: (Theme) -> Unit = {},
): Unit = Column(
    modifier = modifier,
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
) {

    val seedColor: ColorSerial,
    val isAmoled: Boolean = false,
    val primary: ColorSerial? = null,
    val secondary: ColorSerial? = null,
    val tertiary: ColorSerial? = null,
    val neutral: ColorSerial? = null,
    val neutralVariant: ColorSerial? = null,
    val error: ColorSerial? = null,
    val contrastLevel: Double = Contrast.Default.value,
    val platform: DynamicScheme.Platform = DynamicScheme.Platform.Default,
    val animate: Boolean = false,
    val animationSpec: AnimationSpecSerial<ColorSerial>? = null,


    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_primary),
        theme.colorScheme.primary,
    ) { value ->
        theme.copyColorPalette { colorScheme -> colorScheme.copy(primary = value) }
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_on_primary),
        theme.colorScheme.onPrimary,
    ) { value ->
        theme.copyColorPalette { colorScheme -> colorScheme.copy(onPrimary = value) }
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_primary_container),
        theme.colorScheme.primaryContainer,
    ) { value ->
        theme.copyColorPalette { colorScheme -> colorScheme.copy(primaryContainer = value) }
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_on_primary_container),
        theme.colorScheme.onPrimaryContainer,
    ) { value ->
        theme.copyColorPalette { colorScheme -> colorScheme.copy(onPrimaryContainer = value) }
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_inverse_primary),
        theme.colorScheme.inversePrimary,
    ) { value ->
        theme.copyColorPalette { colorScheme -> colorScheme.copy(inversePrimary = value) }
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_secondary),
        theme.colorScheme.secondary,
    ) { value ->
        theme.copyColorPalette { colorScheme -> colorScheme.copy(secondary = value) }
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_on_secondary),
        theme.colorScheme.onSecondary,
    ) { value ->
        theme.copyColorPalette { colorScheme -> colorScheme.copy(onSecondary = value) }
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_secondary_container),
        theme.colorScheme.secondaryContainer,
    ) { value ->
        theme.copyColorPalette { colorScheme -> colorScheme.copy(secondaryContainer = value) }
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_on_secondary_container),
        theme.colorScheme.onSecondaryContainer,
    ) { value ->
        theme.copyColorPalette { colorScheme -> colorScheme.copy(onSecondaryContainer = value) }
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_tertiary),
        theme.colorScheme.tertiary,
    ) { value ->
        theme.copyColorPalette { colorScheme -> colorScheme.copy(tertiary = value) }
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_on_tertiary),
        theme.colorScheme.onTertiary,
    ) { value ->
        theme.copyColorPalette { colorScheme -> colorScheme.copy(onTertiary = value) }
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_tertiary_container),
        theme.colorScheme.tertiaryContainer,
    ) { value ->
        theme.copyColorPalette { colorScheme -> colorScheme.copy(tertiaryContainer = value) }
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_on_tertiary_container),
        theme.colorScheme.onTertiaryContainer,
    ) { value ->
        theme.copyColorPalette { colorScheme -> colorScheme.copy(onTertiaryContainer = value) }
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_background),
        theme.colorScheme.background,
    ) { value ->
        theme.copyColorPalette { colorScheme -> colorScheme.copy(background = value) }
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_on_background),
        theme.colorScheme.onBackground,
    ) { value ->
        theme.copyColorPalette { colorScheme -> colorScheme.copy(onBackground = value) }
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_surface),
        theme.colorScheme.surface,
    ) { value ->
        theme.copyColorPalette { colorScheme -> colorScheme.copy(surface = value) }
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_on_surface),
        theme.colorScheme.onSurface,
    ) { value ->
        theme.copyColorPalette { colorScheme -> colorScheme.copy(onSurface = value) }
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_surface_variant),
        theme.colorScheme.surfaceVariant,
    ) { value ->
        theme.copyColorPalette { colorScheme -> colorScheme.copy(surfaceVariant = value) }
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_on_surface_variant),
        theme.colorScheme.onSurfaceVariant,
    ) { value ->
        theme.copyColorPalette { colorScheme -> colorScheme.copy(onSurfaceVariant = value) }
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_surface_tint),
        theme.colorScheme.surfaceTint,
    ) { value ->
        theme.copyColorPalette { colorScheme -> colorScheme.copy(surfaceTint = value) }
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_inverse_surface),
        theme.colorScheme.inverseSurface,
    ) { value ->
        theme.copyColorPalette { colorScheme -> colorScheme.copy(inverseSurface = value) }
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_inverse_on_surface),
        theme.colorScheme.inverseOnSurface,
    ) { value ->
        theme.copyColorPalette { colorScheme -> colorScheme.copy(inverseOnSurface = value) }
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_error),
        theme.colorScheme.error,
    ) { value ->
        theme.copyColorPalette { colorScheme -> colorScheme.copy(error = value) }
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_on_error),
        theme.colorScheme.onError,
    ) { value ->
        theme.copyColorPalette { colorScheme -> colorScheme.copy(onError = value) }
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_error_container),
        theme.colorScheme.errorContainer,
    ) { value ->
        theme.copyColorPalette { colorScheme -> colorScheme.copy(errorContainer = value) }
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_on_error_container),
        theme.colorScheme.onErrorContainer,
    ) { value ->
        theme.copyColorPalette { colorScheme -> colorScheme.copy(onErrorContainer = value) }
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_outline),
        theme.colorScheme.outline,
    ) { value ->
        theme.copyColorPalette { colorScheme -> colorScheme.copy(outline = value) }
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_outline_variant),
        theme.colorScheme.outlineVariant,
    ) { value ->
        theme.copyColorPalette { colorScheme -> colorScheme.copy(outlineVariant = value) }
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_scrim),
        theme.colorScheme.scrim,
    ) { value ->
        theme.copyColorPalette { colorScheme -> colorScheme.copy(scrim = value) }
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_surface_bright),
        theme.colorScheme.surfaceBright,
    ) { value ->
        theme.copyColorPalette { colorScheme -> colorScheme.copy(surfaceBright = value) }
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_surface_dim),
        theme.colorScheme.surfaceDim,
    ) { value ->
        theme.copyColorPalette { colorScheme -> colorScheme.copy(surfaceDim = value) }
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_surface_container),
        theme.colorScheme.surfaceContainer,
    ) { value ->
        theme.copyColorPalette { colorScheme -> colorScheme.copy(surfaceContainer = value) }
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_surface_container_high),
        theme.colorScheme.surfaceContainerHigh,
    ) { value ->
        theme.copyColorPalette { colorScheme -> colorScheme.copy(surfaceContainerHigh = value) }
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_surface_container_highest),
        theme.colorScheme.surfaceContainerHighest,
    ) { value ->
        theme.copyColorPalette { colorScheme -> colorScheme.copy(surfaceContainerHighest = value) }
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_surface_container_low),
        theme.colorScheme.surfaceContainerLow,
    ) { value ->
        theme.copyColorPalette { colorScheme -> colorScheme.copy(surfaceContainerLow = value) }
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_surface_container_lowest),
        theme.colorScheme.surfaceContainerLowest,
    ) { value ->
        theme.copyColorPalette { colorScheme -> colorScheme.copy(surfaceContainerLowest = value) }
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_primary_fixed),
        theme.colorScheme.primaryFixed,
    ) { value ->
        theme.copyColorPalette { colorScheme -> colorScheme.copy(primaryFixed = value) }
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_primary_fixed_dim),
        theme.colorScheme.primaryFixedDim,
    ) { value ->
        theme.copyColorPalette { colorScheme -> colorScheme.copy(primaryFixedDim = value) }
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_on_primary_fixed),
        theme.colorScheme.onPrimaryFixed,
    ) { value ->
        theme.copyColorPalette { colorScheme -> colorScheme.copy(onPrimaryFixed = value) }
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_on_primary_fixed_variant),
        theme.colorScheme.onPrimaryFixedVariant,
    ) { value ->
        theme.copyColorPalette { colorScheme -> colorScheme.copy(onPrimaryFixedVariant = value) }
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_secondary_fixed),
        theme.colorScheme.secondaryFixed,
    ) { value ->
        theme.copyColorPalette { colorScheme -> colorScheme.copy(secondaryFixed = value) }
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_secondary_fixed_dim),
        theme.colorScheme.secondaryFixedDim,
    ) { value ->
        theme.copyColorPalette { colorScheme -> colorScheme.copy(secondaryFixedDim = value) }
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_on_secondary_fixed),
        theme.colorScheme.onSecondaryFixed,
    ) { value ->
        theme.copyColorPalette { colorScheme -> colorScheme.copy(onSecondaryFixed = value) }
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_on_secondary_fixed_variant),
        theme.colorScheme.onSecondaryFixedVariant,
    ) { value ->
        theme.copyColorPalette { colorScheme -> colorScheme.copy(onSecondaryFixedVariant = value) }
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_tertiary_fixed),
        theme.colorScheme.tertiaryFixed,
    ) { value ->
        theme.copyColorPalette { colorScheme -> colorScheme.copy(tertiaryFixed = value) }
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_tertiary_fixed_dim),
        theme.colorScheme.tertiaryFixedDim,
    ) { value ->
        theme.copyColorPalette { colorScheme -> colorScheme.copy(tertiaryFixedDim = value) }
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_on_tertiary_fixed),
        theme.colorScheme.onTertiaryFixed,
    ) { value ->
        theme.copyColorPalette { colorScheme -> colorScheme.copy(onTertiaryFixed = value) }
    }

    SettingsColorSchemeColor(
        stringResource(Res.string.color_scheme_on_tertiary_fixed_variant),
        theme.colorScheme.onTertiaryFixedVariant,
    ) { value ->
        theme.copyColorPalette { colorScheme -> colorScheme.copy(onTertiaryFixedVariant = value) }
    }
}

private fun Theme.copyColorPalette(block: (ColorScheme) -> ColorScheme): Theme =
    copy(
        colorPalette =
            if (isDark != true) {
                colorPalette.copy(
                    lightColorScheme = block(
                        colorPalette.lightColorScheme,
                    ),
                )
            }
            else {
                colorPalette.copy(
                    darkColorScheme = block(
                        colorPalette.lightColorScheme,
                    ),
                )
            },
    )

@Preview
@Composable
public fun PreviewSettingsDynamicColorPaletteScreen(): Unit = SettingsDynamicColorPaletteScreen()


