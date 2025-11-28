@file:Suppress("ComposeCompositionLocalUsage")

package clib.presentation.locale

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import klib.data.location.locale.Locale

internal expect object LocalAppLocale {

    val current: Locale @Composable get

    @Composable
    infix fun provides(value: Locale): ProvidedValue<*>
}
