package clib.presentation.theme.model

import androidx.compose.ui.graphics.Color

public data class Theme(
    val isDarkMode: Boolean,
    val isHighContrast: Boolean = false,
)

public sealed class ThemeMode {
    public object Light : ThemeMode()
    public object Dark : ThemeMode()
    public object System : ThemeMode()
    public object HighContrast : ThemeMode()
}
