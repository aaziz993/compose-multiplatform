package clib.presentation.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import clib.presentation.noLocalProvidedFor
import clib.presentation.theme.model.Theme

public var customAppTheme: Theme? by mutableStateOf(null)

@Suppress("ComposeCompositionLocalUsage")
private val LocalTheme = staticCompositionLocalOf<Theme> { noLocalProvidedFor("LocalTheme") }

public object LocalAppTheme {

    /** Returns the current [Theme] in composition. */
    public val current: Theme
        @Composable
        get() = LocalTheme.current

    /**
     * Creates a [ProvidedValue] that provides a given [Theme] to the composition.
     */
    @Composable
    public infix fun provides(value: Theme?): ProvidedValue<*> {
        val theme = value ?: Theme.SYSTEM
        return LocalTheme.provides(theme)
    }
}
