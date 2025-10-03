package clib.presentation.locale

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.intl.Locale

@Suppress("ClassName")
internal external object window {

    @Suppress("ObjectPropertyName")
    var __customLocale: String?
}

public actual object LocalAppLocale {

    private val LocalAppLocale = staticCompositionLocalOf { Locale.current }
    public actual val current: String
        @Composable
        get() = LocalAppLocale.current.toString()

    @Composable
    public actual infix fun provides(value: String?): ProvidedValue<*> {
        window.__customLocale = value?.replace('_', '-')
        return LocalAppLocale.provides(Locale.current)
    }
}
