package presentation.components.app.viewmodel

import androidx.lifecycle.SavedStateHandle
import clib.presentation.theme.ThemeState
import clib.presentation.viewmodel.AbstractViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
public class AppViewModel(
    override val savedStateHandle: SavedStateHandle = SavedStateHandle(),
) : AbstractViewModel<AppAction>() {

    public val themeState: ThemeState = ThemeState()

    override fun action(action: AppAction): Unit = when (action) {
        is AppAction.SetTheme -> themeState.theme = action.theme
        is AppAction.SetLocale -> {}
    }
}
