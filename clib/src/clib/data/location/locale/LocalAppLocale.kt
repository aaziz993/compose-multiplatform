@file:Suppress("ComposeCompositionLocalUsage")

package clib.data.location.locale

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import klib.data.location.locale.Locale

public var customAppLocale: Locale? by mutableStateOf(null)

public expect object LocalAppLocale {

    public val current: Locale @Composable get

    @Composable
    public infix fun provides(value: Locale?): ProvidedValue<*>
}
