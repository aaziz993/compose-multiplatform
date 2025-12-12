package clib.presentation.components

import kotlinx.serialization.Serializable

@Serializable
public data class AppBar(
    public val isTitle: Boolean = true,
    public val isSupport: Boolean = true,
    public val isTheme: Boolean = true,
    public val isLocale: Boolean = true,
    public val isAvatar: Boolean = true,
)
