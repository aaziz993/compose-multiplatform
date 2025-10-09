package clib.presentation.theme.locale

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.staticCompositionLocalOf
import klib.data.location.locale.Locale
import klib.data.location.locale.current
import klib.data.location.locale.setCurrent

public actual object LocalAppLocale {
    private val default = Locale.current
    private val LocalAppLocale = staticCompositionLocalOf { default }
    public actual val current: Locale
        @Composable get() = LocalAppLocale.current

    @Composable
    public actual infix fun provides(value: Locale?): ProvidedValue<*> {
        val newLocale = value ?: default
        Locale.setCurrent(newLocale)

        return LocalAppLocale.provides(newLocale)
    }
}
