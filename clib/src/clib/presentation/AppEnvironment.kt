@file:Suppress("ComposeCompositionLocalUsage")

package clib.presentation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import clib.data.location.locale.LocalAppLocale
import clib.data.location.locale.customAppLocale
import clib.presentation.theme.LocalAppDensity
import clib.presentation.theme.customAppDensity
import clib.presentation.theme.darkColorScheme
import clib.presentation.theme.highContrastColorScheme
import clib.presentation.theme.lightColorScheme
import clib.presentation.theme.model.ThemeMode
import clib.presentation.theme.ThemeState

@Composable
public fun AppEnvironment(
    themeState: ThemeState = ThemeState(),
    lightTheme: ColorScheme = lightColorScheme,
    darkTheme: ColorScheme = darkColorScheme,
    shapes: Shapes = MaterialTheme.shapes,
    typography: Typography = MaterialTheme.typography,
    content: @Composable () -> Unit
) {
    val themeMode by themeState.themeMode.collectAsState()
    val theme by themeState.theme.collectAsState()
    // This is the secret sauce - listening to system changes
    val systemDarkMode = isSystemInDarkTheme()

    val colorScheme = when {
        theme.isHighContrast -> highContrastColorScheme(theme.isDarkMode, lightTheme, darkTheme)
        theme.isDarkMode -> darkColorScheme
        else -> lightColorScheme
    }

    CompositionLocalProvider(
        LocalAppLocale provides customAppLocale,
        LocalAppDensity provides customAppDensity,
    ) {
        key(customAppLocale, customAppDensity) {
            MaterialTheme(
                colorScheme = colorScheme,
                shapes = shapes,
                typography = typography,
                content = content,
            )
        }
    }

    LaunchedEffect(systemDarkMode, themeMode) {
        if (themeMode == ThemeMode.System)
            themeState.setThemeMode(ThemeMode.System, systemDarkMode)
    }
}


