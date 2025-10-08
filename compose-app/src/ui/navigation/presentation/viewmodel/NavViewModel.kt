package ui.navigation.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import clib.data.type.collections.restartableflow.RestartableStateFlow
import clib.presentation.viewmodel.AbstractViewModel
import org.koin.android.annotation.KoinViewModel
import kotlinx.coroutines.flow.update

@KoinViewModel
public class NavViewModel(
    override val savedStateHandle: SavedStateHandle = SavedStateHandle(),
) : AbstractViewModel<NavAction>() {

    public val state: RestartableStateFlow<NavState>
        field = viewModelMutableStateFlow<NavState>(NavState())

    override fun action(action: NavAction): Unit = when (action) {
        is NavAction.OpenDrawer -> state.update { it.copy(drawerOpen = action.value) }
    }
}
