@file:Suppress("ComposeCompositionLocalUsage")

package clib.presentation

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.key
import androidx.compose.runtime.staticCompositionLocalOf
import clib.data.location.locale.LocalAppLocale
import clib.data.location.locale.customAppLocale
import clib.presentation.theme.LocalAppDensity
import clib.presentation.theme.LocalAppTheme
import clib.presentation.theme.customAppDensity
import clib.presentation.theme.customAppTheme
import clib.presentation.theme.darkColorScheme
import clib.presentation.theme.lightColorScheme
import clib.presentation.theme.model.Theme
import clib.presentation.theme.systemTheme

@Composable
public fun AppEnvironment(
    lightTheme: ColorScheme = lightColorScheme,
    darkTheme: ColorScheme = darkColorScheme,
    shapes: Shapes = MaterialTheme.shapes,
    typography: Typography = MaterialTheme.typography,
    content: @Composable () -> Unit
): Unit = CompositionLocalProvider(
    LocalAppLocale provides customAppLocale,
    LocalAppTheme provides customAppTheme,
    LocalAppDensity provides customAppDensity,
) {
    key(customAppLocale, customAppTheme, customAppDensity) {
        val colorScheme = when (LocalAppTheme.current) {
            Theme.LIGHT -> lightTheme
            Theme.DARK -> darkTheme
            Theme.SYSTEM -> systemTheme(lightTheme, darkTheme)
        }

        MaterialTheme(colorScheme, shapes, typography) {
            content()
        }
    }
}
