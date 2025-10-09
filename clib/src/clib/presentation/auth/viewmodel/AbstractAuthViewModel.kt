package clib.presentation.auth.viewmodel

import clib.data.type.collections.restartableflow.RestartableStateFlow
import clib.presentation.viewmodel.AbstractViewModel
import klib.data.type.auth.model.Auth
import klib.data.type.auth.model.User
import kotlinx.coroutines.flow.update

public abstract class AbstractAuthViewModel : AbstractViewModel<AuthAction>() {

    protected open suspend fun initialState(): Auth = Auth()

    public val state: RestartableStateFlow<Auth>
        field = viewModelMutableStateFlow(Auth()) {
            initialState()
        }

    override fun action(action: AuthAction): Unit = when (action) {
        is AuthAction.SetAuth -> setAuth(action.value)
        is AuthAction.SetProvider -> setProvider(action.value)
        is AuthAction.SetUser -> setUser(action.value)
    }

    private fun setAuth(value: Auth) = state.update { value }
    private fun setProvider(value: String?) = state.update { it.copy(provider = value) }
    private fun setUser(value: User?) = state.update { it.copy(user = value) }
}
