package clib.presentation.theme.density

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density

public object LocalAppDensity {

    public val current: Density
        @Composable
        get() = LocalDensity.current

    @Composable
    public infix fun provides(value: Density?): ProvidedValue<*> {
        val new = value ?: LocalDensity.current
        return LocalDensity.provides(new)
    }
}
