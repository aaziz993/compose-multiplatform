package clib.presentation.appbar.model

import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.dp
import clib.presentation.appbar.TopAppBarColorsSerial
import clib.presentation.theme.LocalAppTheme
import kotlinx.serialization.Serializable

@Immutable
@Serializable
public data class AppBar(
    public val isTitle: Boolean = true,
    public val isSupport: Boolean = true,
    public val isTheme: Boolean = true,
    public val isLocale: Boolean = true,
    public val isAvatar: Boolean = true,
    public val lightColors: TopAppBarColorsSerial = lightColorScheme().let { colors ->
        TopAppBarColors(
            colors.surface,
            colors.surfaceColorAtElevation(3.dp),
            colors.onSurface,
            colors.onSurface,
            colors.onSurface,
            colors.onSurfaceVariant,
        )
    },
    public val lightColorsHighContrast: TopAppBarColorsSerial = lightColors,
    public val darkColors: TopAppBarColorsSerial = darkColorScheme().let { colors ->
        TopAppBarColors(
            colors.surface,
            colors.surfaceColorAtElevation(3.dp),
            colors.onSurface,
            colors.onSurface,
            colors.onSurface,
            colors.onSurfaceVariant,
        )
    },
    public val darkColorsHighContrast: TopAppBarColorsSerial = darkColors,
) {

    public val colors: TopAppBarColors
        @Composable
        get() = if (LocalAppTheme.current) darkColors else lightColors

    @Composable
    public fun copyColors(isHighContrast: Boolean): (TopAppBarColors) -> AppBar =
        if (isHighContrast) {
            if (LocalAppTheme.current) { colors -> copy(darkColorsHighContrast = colors) }
            else { colors -> copy(lightColorsHighContrast = colors) }
        }
        else {
            if (LocalAppTheme.current) { colors -> copy(darkColors = colors) }
            else { colors -> copy(lightColors = colors) }
        }
}
