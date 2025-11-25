package clib.presentation.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue

public expect object LocalAppTheme {

    public val current: Boolean @Composable get

    @Composable
    public infix fun provides(value: Boolean?): ProvidedValue<*>
}
