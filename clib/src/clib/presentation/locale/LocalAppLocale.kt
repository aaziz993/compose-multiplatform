@file:Suppress("ComposeCompositionLocalUsage")

package clib.presentation.locale

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import clib.generated.resources.Res
import klib.data.location.locale.Locale
import org.jetbrains.compose.resources.ResourceItem
import org.jetbrains.compose.resources.StringResource

public var customAppLocale: Locale? by mutableStateOf(null)

public expect object LocalAppLocale {

    public val current: Locale @Composable get

    @Composable
    public infix fun provides(value: Locale?): ProvidedValue<*>
}
