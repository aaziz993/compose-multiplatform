package clib.presentation.appbar.model

import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.dp
import clib.data.type.DpSerial
import clib.presentation.appbar.TopAppBarColorsSerial
import clib.presentation.theme.LocalAppTheme
import kotlinx.serialization.Serializable

@Immutable
@Serializable
public data class AppBar(
    public val mode: AppBarMode = AppBarMode.Default,
    public val expandedHeight: DpSerial = 64.dp,
    public val isTitle: Boolean = true,
    public val isSupport: Boolean = true,
    public val isTheme: Boolean = true,
    public val isLocale: Boolean = true,
    public val isAvatar: Boolean = true,
    val lightColors: TopAppBarColorsSerial? = null,
    val lightColorsHighContrast: TopAppBarColorsSerial? = null,
    val darkColors: TopAppBarColorsSerial? = null,
    val darkColorsHighContrast: TopAppBarColorsSerial? = null,
    val dynamicColors: TopAppBarColorsSerial? = null,
    val dynamicColorsHighContrast: TopAppBarColorsSerial? = null,
) {

    @Composable
    public fun colors(isDynamic: Boolean, isHighContrast: Boolean): TopAppBarColors =
        if (isDynamic) {
            if (isHighContrast) dynamicColorsHighContrast else dynamicColors
        }
        else {
            val (lightColors, darkColors) =
                if (isHighContrast) lightColorsHighContrast to darkColorsHighContrast else lightColors to darkColors
            if (LocalAppTheme.current) darkColors else lightColors
        } ?: TopAppBarDefaults.topAppBarColors()

    @Composable
    public fun copyColors(isDynamic: Boolean, isHighContrast: Boolean): (TopAppBarColors) -> AppBar =
        if (isDynamic) {
            if (isHighContrast) { colors -> copy(dynamicColorsHighContrast = colors) }
            else { colors -> copy(dynamicColors = colors) }
        }
        else {
            if (isHighContrast) {
                if (LocalAppTheme.current) { colors -> copy(darkColorsHighContrast = colors) }
                else { colors -> copy(lightColorsHighContrast = colors) }
            }
            else {
                if (LocalAppTheme.current) { colors -> copy(darkColors = colors) }
                else { colors -> copy(lightColors = colors) }
            }
        }
}
