package clib.presentation

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.key
import clib.presentation.locale.LocalAppLocale
import clib.presentation.locale.customAppLocale
import clib.presentation.theme.LocalAppDensity
import clib.presentation.theme.customAppDensity
import clib.presentation.theme.darkColorScheme
import clib.presentation.theme.lightColorScheme
import clib.presentation.theme.systemTheme

@Composable
public fun AppEnvironment(
    darkTheme: ColorScheme = darkColorScheme,
    lightTheme: ColorScheme = lightColorScheme,
    content: @Composable () -> Unit
) {
    MaterialTheme(systemTheme(lightTheme, darkTheme)) {
        CompositionLocalProvider(
            LocalAppLocale provides customAppLocale,
            LocalAppDensity provides customAppDensity,
        ) {
            key(customAppLocale, customAppDensity) {
                content()
            }
        }
    }
}
