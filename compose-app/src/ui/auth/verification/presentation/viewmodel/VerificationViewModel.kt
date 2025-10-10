package ui.auth.verification.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import clib.data.type.collections.restartableflow.RestartableStateFlow
import clib.presentation.auth.stateholder.AuthAction
import clib.presentation.auth.stateholder.AuthStateHolder
import clib.presentation.viewmodel.AbstractViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

public class VerificationViewModel(
    private val authStateHolder: AuthStateHolder,
    override val savedStateHandle: SavedStateHandle = SavedStateHandle(),
) : AbstractViewModel<VerificationAction>() {

    public val state: RestartableStateFlow<VerificationState>
        field = viewModelMutableStateFlow(VerificationState())

    override fun action(action: VerificationAction): Unit = when (action) {
        is VerificationAction.SetIdImage -> setIdImage(action.value)
        is VerificationAction.SetUserImage -> setUserImage(action.value)
        is VerificationAction.Confirm -> confirm()
    }

    private fun setIdImage(value: String) = state.update { it.copy(idImage = value) }
    private fun setUserImage(value: String) = state.update { it.copy(userImage = value) }

    private fun confirm() {
        viewModelScope.launch {
            val user = authStateHolder.state.value.user!!
            authStateHolder.action(
                AuthAction.SetUser(user.copy(roles = user.roles + "VerifiedUser")),
            )
        }
    }
}
