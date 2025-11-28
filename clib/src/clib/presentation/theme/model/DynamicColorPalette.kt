package clib.presentation.theme.model

import clib.data.type.Color
import com.materialkolor.Contrast
import com.materialkolor.scheme.DynamicScheme
import kotlinx.serialization.Serializable

@Serializable
public data class DynamicColorPalette(
    val seedColor: Color,
    val isAmoled: Boolean = false,
    val primary: Color? = null,
    val secondary: Color? = null,
    val tertiary: Color? = null,
    val neutral: Color? = null,
    val neutralVariant: Color? = null,
    val error: Color? = null,
    val contrastLevel: Double = Contrast.Default.value,
    val platform: DynamicScheme.Platform = DynamicScheme.Platform.Default,
    val animate: Boolean = false,
)
