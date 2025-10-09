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
        is LoginAction.SetUsername -> setUsername(action.value)
        is LoginAction.SetPassword -> setPassword(action.value)
        is LoginAction.ShowPassword -> showPassword(action.value)
        is LoginAction.Login -> login()
    }

    private fun setUsername(value: String) = state.update { it.copy(username = value, error = null) }

    private fun setPassword(value: String) = state.update { it.copy(password = value, error = null) }

    private fun showPassword(value: Boolean) = state.update { it.copy(showPassword = value, error = null) }

    private fun login() {
        viewModelScope.launch {
            if (state.value.username == "admin" && state.value.password == "admin")
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
            else state.update { it.copy(error = "Invalid username or password") }
        }
    }
}
