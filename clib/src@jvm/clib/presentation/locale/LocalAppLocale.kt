package clib.presentation.locale

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.staticCompositionLocalOf
import klib.data.location.locale.Locale
import klib.data.location.locale.current
import klib.data.location.locale.setCurrent

internal actual object LocalAppLocale {

    private val LocalAppLocale = staticCompositionLocalOf { Locale.current }
    actual val current: Locale
        @Composable get() = LocalAppLocale.current

    @Composable
    actual infix fun provides(value: Locale): ProvidedValue<*> {
        Locale.setCurrent(value)

        return LocalAppLocale.provides(value)
    }
}
