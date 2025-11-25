package clib.presentation.theme

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import clib.presentation.noLocalProvidedFor

@Suppress("ComposeCompositionLocalUsage")
public val LocalThemeState: ProvidableCompositionLocal<ThemeState> =
    staticCompositionLocalOf { noLocalProvidedFor("LocalThemeState") }
