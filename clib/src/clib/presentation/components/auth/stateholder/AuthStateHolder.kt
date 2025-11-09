package clib.presentation.components.auth.stateholder

import klib.data.type.auth.model.Auth
import klib.data.type.auth.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

public class AuthStateHolder(auth: Auth = Auth()) {

    public val state: StateFlow<Auth>
        field = MutableStateFlow(auth)

    public fun action(action: AuthAction): Unit = when (action) {
        is AuthAction.SetAuth -> setAuth(action.value)
        is AuthAction.SetProvider -> setProvider(action.value)
        is AuthAction.SetUser -> setUser(action.value)
    }

    private fun setAuth(value: Auth) = state.update { value }

    private fun setProvider(value: String?) = state.update { it.copy(provider = value) }

    private fun setUser(value: User?) = state.update { it.copy(user = value) }
}
