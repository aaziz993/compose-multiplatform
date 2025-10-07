package clib.presentation.theme

import clib.presentation.theme.model.Theme
import clib.presentation.theme.model.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

public class ThemeState(
    initialMode: ThemeMode = ThemeMode.System,
    initialTheme: Theme = Theme(isDarkMode = false)
) {

    public val theme: StateFlow<Theme>
        field = MutableStateFlow<Theme>(initialTheme)

    public val themeMode: StateFlow<ThemeMode>
        field = MutableStateFlow<ThemeMode>(initialMode)

    public fun setThemeMode(mode: ThemeMode, isSystemDark: Boolean) {
        themeMode.value = mode

        val shouldUseDark = when (mode) {
            is ThemeMode.Light -> false
            is ThemeMode.Dark -> true
            is ThemeMode.System -> isSystemDark
            is ThemeMode.HighContrast -> true
        }

        theme.value = theme.value.copy(
            isDarkMode = shouldUseDark,
            isHighContrast = mode is ThemeMode.HighContrast,
        )
    }
}
