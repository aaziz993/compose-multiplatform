package clib.presentation.auth.viewmodel

import clib.data.type.collections.restartableflow.RestartableStateFlow
import clib.presentation.viewmodel.AbstractViewModel
import klib.data.type.auth.User
import kotlinx.coroutines.flow.update

public abstract class AbstractUserViewModel : AbstractViewModel<UserAction>() {

    protected open suspend fun initialState(): User? = null

    public val state: RestartableStateFlow<User?>
        field = viewModelMutableStateFlow(null) {
            initialState()
        }

    override fun action(action: UserAction): Unit = when (action) {
        is UserAction.SetUser -> setUser(action.value)
    }

    private fun setUser(value: User?) = state.update { value }
}
