package clib.presentation.theme.model

public data class Theme(
    val mode: ThemeMode = ThemeMode.SYSTEM,
    val isHighContrast: Boolean = false,
)
