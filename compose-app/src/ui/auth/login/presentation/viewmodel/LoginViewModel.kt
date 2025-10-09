package ui.auth.login.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import clib.data.type.collections.restartableflow.RestartableStateFlow
import clib.presentation.auth.viewmodel.UserAction
import clib.presentation.auth.viewmodel.AbstractUserViewModel
import clib.presentation.viewmodel.AbstractViewModel
import klib.data.type.auth.User
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import ui.auth.presentation.viewmodel.UserViewModel

@KoinViewModel
public class LoginViewModel(
    private val userViewModel: UserViewModel
) : AbstractViewModel<LoginAction>() {

    public val state: RestartableStateFlow<LoginState>
        field = viewModelMutableStateFlow(LoginState())

    override fun action(action: LoginAction): Unit = when (action) {
        is LoginAction.SetPinCode -> setPinCode(action.value)
        is LoginAction.ShowPinCode -> showPinCode(action.value)
        is LoginAction.Login -> login()
    }

    private fun setPinCode(value: String) = state.update { it.copy(pinCode = value, error = null) }

    private fun showPinCode(value: Boolean) = state.update { it.copy(showPinCode = value, error = null) }

    private fun login() {
        viewModelScope.launch {
            if (state.value.pinCode == "7890")
                userViewModel.action(
                    UserAction.SetUser(
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
