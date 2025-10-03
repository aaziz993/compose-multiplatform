package clib.presentation.locale

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import clib.presentation.theme.LocalAppDensity
import clib.presentation.theme.customAppDensity

public var customAppLocale: String? by mutableStateOf(null)

public expect object LocalAppLocale {

    public val current: String
        @Composable
        get

    @Composable
    public infix fun provides(value: String?): ProvidedValue<*>
}
