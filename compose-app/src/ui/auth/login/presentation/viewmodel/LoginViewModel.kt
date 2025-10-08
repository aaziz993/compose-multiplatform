package ui.auth.login.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import clib.data.type.collections.restartableflow.RestartableStateFlow
import clib.presentation.viewmodel.AbstractViewModel
import klib.data.type.auth.User
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
public class LoginViewModel : AbstractViewModel<LoginAction>() {

    public val state: RestartableStateFlow<LoginState>
        field = viewModelMutableStateFlow(LoginState())

    override fun action(action: LoginAction): Unit = when (action) {
        is LoginAction.SetUsername -> state.update { it.copy(username = action.value, isError = false) }
        is LoginAction.SetPassword -> state.update { it.copy(password = action.value, isError = false) }
        is LoginAction.ShowPassword -> state.update { it.copy(showPassword = action.value, isError = false) }
        is LoginAction.Login -> login()
    }

    private fun login() {
        viewModelScope.launch {
            state.update {
                it.copy(
                    user = User(
                        username = state.value.username,
                        firstName = "John",
                        lastName = "Doe",
                        roles = setOf("User"),
                    ),
                )
            }
        }
    }
}
