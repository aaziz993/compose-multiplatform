package clib.presentation.theme.viewmodel

import androidx.lifecycle.SavedStateHandle
import clib.data.type.collections.restartableflow.RestartableStateFlow
import clib.presentation.theme.model.Theme
import clib.presentation.theme.model.ThemeMode
import clib.presentation.viewmodel.AbstractViewModel
import kotlinx.coroutines.flow.update

public class ThemeViewModel(
    theme: Theme = Theme(),
    override val savedStateHandle: SavedStateHandle = SavedStateHandle()
) : AbstractViewModel<ThemeAction>() {

    public val state: RestartableStateFlow<Theme>
        field = viewModelMutableStateFlow(theme)

    override fun action(action: ThemeAction): Unit = when (action) {
        is ThemeAction.SetTheme -> setTheme(action.value)
        is ThemeAction.SetMode -> setMode(action.value)
        is ThemeAction.SetHighContrast -> setHighContrast(action.value)
    }

    private fun setTheme(value: Theme): Unit = state.update { value }

    private fun setMode(value: ThemeMode): Unit = state.update { it.copy(mode = value) }

    private fun setHighContrast(value: Boolean): Unit = state.update { it.copy(isHighContrast = value) }
}
