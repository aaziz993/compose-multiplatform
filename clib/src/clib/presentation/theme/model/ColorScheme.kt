package clib.presentation.theme.model

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import clib.data.type.primitives.color.invert

/** Converts color scheme to amoled.
 *
 * @receiver Color scheme to convert to amoled.
 * @param isDark Current theme is dark.
 * @return Amoled color scheme.
 */
public fun ColorScheme.toAmoled(isDark: Boolean): ColorScheme =
    copy(
        background = if (isDark) Color.Black else background,
        onBackground = if (isDark) Color.White else onBackground,
        surface = if (isDark) Color.Black else surface,
        onSurface = if (isDark) Color.White else onSurface,
    )

/** Converts color scheme to high constrast.
 *
 * @receiver Color scheme to convert to high contrast.
 * @param isDark Current theme is dark.
 * @return High contrast color scheme.
 */
public fun ColorScheme.toHighContrast(isDark: Boolean): ColorScheme =
    if (isDark) copy(
        primary = Color.White,
        onPrimary = Color.Black,
        primaryContainer = Color.White,
        onPrimaryContainer = Color.Black,
        surface = Color.Black,
        onSurface = Color.White,
        background = Color.Black,
        onBackground = Color.White,
        outline = Color.White,
        outlineVariant = Color.White,
    )
    else copy(
        primary = Color.Black,
        onPrimary = Color.White,
        primaryContainer = Color.Black,
        onPrimaryContainer = Color.White,
        surface = Color.White,
        onSurface = Color.Black,
        background = Color.White,
        onBackground = Color.Black,
        outline = Color.Black,
        outlineVariant = Color.Black,
    )

/** Inverts colors of color scheme.
 *
 * @receiver Color scheme to invert colors.
 */
public fun ColorScheme.invert(): ColorScheme = ColorScheme(
    primary.invert(),
    onPrimary.invert(),
    primaryContainer.invert(),
    onPrimaryContainer.invert(),
    inversePrimary.invert(),
    secondary.invert(),
    onSecondary.invert(),
    secondaryContainer.invert(),
    onSecondaryContainer.invert(),
    tertiary.invert(),
    onTertiary.invert(),
    tertiaryContainer.invert(),
    onTertiaryContainer.invert(),
    background.invert(),
    onBackground.invert(),
    surface.invert(),
    onSurface.invert(),
    surfaceVariant.invert(),
    onSurfaceVariant.invert(),
    surfaceTint.invert(),
    inverseSurface.invert(),
    inverseOnSurface.invert(),
    error.invert(),
    onError.invert(),
    errorContainer.invert(),
    onErrorContainer.invert(),
    outline.invert(),
    outlineVariant.invert(),
    scrim.invert(),
    surfaceBright.invert(),
    surfaceDim.invert(),
    surfaceContainer.invert(),
    surfaceContainerHigh.invert(),
    surfaceContainerHighest.invert(),
    surfaceContainerLow.invert(),
    surfaceContainerLowest.invert(),
    primaryFixed.invert(),
    primaryFixedDim.invert(),
    onPrimaryFixed.invert(),
    onPrimaryFixedVariant.invert(),
    secondaryFixed.invert(),
    secondaryFixedDim.invert(),
    onSecondaryFixed.invert(),
    onSecondaryFixedVariant.invert(),
    tertiaryFixed.invert(),
    tertiaryFixedDim.invert(),
    onTertiaryFixed.invert(),
    onTertiaryFixedVariant.invert(),
)
