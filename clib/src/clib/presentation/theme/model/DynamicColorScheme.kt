package clib.presentation.theme.model

import androidx.compose.runtime.Immutable
import clib.data.type.primitives.color.ColorSerial
import kotlinx.serialization.Serializable

@Immutable
@Serializable
public data class DynamicColorScheme(
    val seedColor: ColorSerial,
    val primary: ColorSerial? = null,
    val secondary: ColorSerial? = null,
    val tertiary: ColorSerial? = null,
    val neutral: ColorSerial? = null,
    val neutralVariant: ColorSerial? = null,
    val error: ColorSerial? = null,
)
