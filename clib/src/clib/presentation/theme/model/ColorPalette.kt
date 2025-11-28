package clib.presentation.theme.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
public data class ColorPalette(
    val lightColorScheme: ColorScheme? = null,
    val darkColorScheme: ColorScheme? = null,
)
