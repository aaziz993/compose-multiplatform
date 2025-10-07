package clib.presentation.theme.model

import kotlinx.serialization.Serializable

@Serializable
public data class Theme(
    val mode: ThemeMode = ThemeMode.SYSTEM,
    val isHighContrast: Boolean = false,
)
