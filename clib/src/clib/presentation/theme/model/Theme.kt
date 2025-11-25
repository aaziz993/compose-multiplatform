package clib.presentation.theme.model

import kotlinx.serialization.Serializable

@Serializable
public data class Theme(
    public val isDark: Boolean? = null,
    public val colorPalette: ColorPalette = StaticColorPalette(),
)


