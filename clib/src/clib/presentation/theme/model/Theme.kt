package clib.presentation.theme.model

import com.materialkolor.Contrast
import com.materialkolor.scheme.DynamicScheme
import kotlinx.serialization.Serializable

@Serializable
public sealed class BaseTheme {

    public abstract val isDark: Boolean?
}

@Serializable
public data class Theme(
    override val isDark: Boolean? = null,
    val lightColorScheme: ColorScheme? = null,
    val darkColorScheme: ColorScheme? = null,
) : BaseTheme()

@Serializable
public data class DynamicTheme(
    override val isDark: Boolean? = null,
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
) : BaseTheme()
