package clib.presentation.theme.stateholder

import clib.presentation.theme.model.Theme
import clib.presentation.theme.model.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

public class ThemeStateHolder(theme: Theme = Theme()) {

    public val state: StateFlow<Theme>
        field = MutableStateFlow(theme)

    public fun action(action: ThemeAction): Unit = when (action) {
        is ThemeAction.SetTheme -> setTheme(action.value)
        is ThemeAction.SetMode -> setMode(action.value)
        is ThemeAction.SetHighContrast -> setHighContrast(action.value)
    }

    private fun setTheme(value: Theme): Unit = state.update { value }

    private fun setMode(value: ThemeMode): Unit = state.update { it.copy(mode = value) }

    private fun setHighContrast(value: Boolean): Unit = state.update { it.copy(isHighContrast = value) }
}
