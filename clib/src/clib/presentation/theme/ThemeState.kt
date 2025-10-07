package clib.presentation.theme

import clib.presentation.theme.model.Theme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

public class ThemeState(initial: Theme = Theme()) {

    public val theme: StateFlow<Theme>
        field = MutableStateFlow<Theme>(initial)

    public fun setTheme(value: Theme) {
        theme.value = value
    }
}
