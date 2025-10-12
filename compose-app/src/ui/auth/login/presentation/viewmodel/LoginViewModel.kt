package ui.auth.login.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import clib.data.type.collections.restartableflow.RestartableStateFlow
import clib.presentation.auth.stateholder.AuthAction
import clib.presentation.auth.stateholder.AuthStateHolder
import clib.presentation.viewmodel.AbstractViewModel
import klib.data.type.auth.model.User
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
public class LoginViewModel(
    private val authStateHolder: AuthStateHolder
) : AbstractViewModel<LoginAction>() {

    public val state: RestartableStateFlow<LoginState>
        field = viewModelMutableStateFlow(LoginState())

    override fun action(action: LoginAction): Unit = when (action) {
        is LoginAction.SetPinCode -> setPinCode(action.value)
        is LoginAction.ShowPinCode -> showPinCode(action.value)
        is LoginAction.Login -> login()
    }

    private fun setPinCode(value: String) = state.update<LoginState> { it.copy(pinCode = value, error = null) }

    private fun showPinCode(value: Boolean) = state.update { it.copy(showPinCode = value, error = null) }

    private fun login() {
        viewModelScope.launch {
            if (state.value.pinCode == "7890")
                authStateHolder.action(
                    AuthAction.SetUser(
                        User(
                            username = "jogn.doe@gmail.com",
                            firstName = "John",
                            lastName = "Doe",
                            roles = setOf("User"),
                        ),
                    ),
                )
            else state.update { it.copy(error = "Invalid pin code") }
        }
    }
}
