package clib.presentation.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue

internal expect object LocalAppTheme {

    val current: Boolean @Composable get

    @Composable
    infix fun provides(value: Boolean?): ProvidedValue<*>
}
