@file:Suppress("ComposeCompositionLocalUsage")

package clib.data.location.locale

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalDensity
import klib.data.location.locale.Locale
import klib.data.location.locale.current

public var customAppLocale: Locale? by mutableStateOf(null)

public object LocalAppLocale {

    private val LocalAppLocale = staticCompositionLocalOf { Locale.current }
    public val current: Locale
        @Composable
        get() = LocalAppLocale.current

    @Composable
    public infix fun provides(value: Locale?): ProvidedValue<*> {
        val new = value ?: LocalAppLocale.current
        return LocalAppLocale.provides(new)
    }
}
