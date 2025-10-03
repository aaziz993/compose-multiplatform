package clib.presentation.locale

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.staticCompositionLocalOf
import java.util.Locale

public actual object LocalAppLocale {

    private var default: Locale? = null
    private val LocalAppLocale = staticCompositionLocalOf { Locale.getDefault().toString() }
    public actual val current: String
        @Composable
        get() = LocalAppLocale.current

    @Composable
    public actual infix fun provides(value: String?): ProvidedValue<*> {
        if (default == null) {
            default = Locale.getDefault()
        }
        val new = when (value) {
            null -> default!!
            else -> Locale(value)
        }
        Locale.setDefault(new)
        return LocalAppLocale.provides(new.toString())
    }
}
