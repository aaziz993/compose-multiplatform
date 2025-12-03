package clib.presentation.theme.model

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
public data class ColorPalette(
    val lightColorScheme: ColorSchemeSerial = lightColorScheme(),
    val darkColorScheme: ColorSchemeSerial = darkColorScheme(),
)
