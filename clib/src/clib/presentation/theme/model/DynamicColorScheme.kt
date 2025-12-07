package clib.presentation.theme.model

import androidx.compose.runtime.Immutable
import clib.data.type.ColorSerial
import com.materialkolor.Contrast
import com.materialkolor.scheme.DynamicScheme
import kotlinx.serialization.Serializable

@Immutable
@Serializable
public data class DynamicColorScheme(
    val seedColor: ColorSerial,
    val isAmoled: Boolean = false,
    val primary: ColorSerial? = null,
    val secondary: ColorSerial? = null,
    val tertiary: ColorSerial? = null,
    val neutral: ColorSerial? = null,
    val neutralVariant: ColorSerial? = null,
    val error: ColorSerial? = null,
    val contrastLevel: Double = Contrast.Default.value,
    val platform: DynamicScheme.Platform = DynamicScheme.Platform.Default,
    val animate: Boolean = false,
    val animationSpec: AnimationSpecSerial<ColorSerial>? = null,
)
