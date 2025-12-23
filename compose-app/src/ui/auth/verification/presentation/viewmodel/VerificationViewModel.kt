package ui.auth.verification.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import klib.data.type.collections.restartableflow.RestartableStateFlow
import clib.presentation.auth.AuthState
import clib.presentation.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
public class VerificationViewModel(
    private val authState: AuthState,
    override val savedStateHandle: SavedStateHandle = SavedStateHandle(),
) : ViewModel<VerificationAction>() {

    public val state: RestartableStateFlow<VerificationState>
        field = MutableStateFlow(VerificationState()).onStartStateIn { it }

    override fun action(action: VerificationAction): Unit = when (action) {
        is VerificationAction.SetIdUri -> setIdUri(action.value)
        is VerificationAction.SetUserUri -> setUserUri(action.value)
        is VerificationAction.Confirm -> confirm()
    }

    private fun setIdUri(value: String) = state.update { it.copy(idUri = value) }
    private fun setUserUri(value: String) = state.update { it.copy(userUri = value) }

    private fun confirm() {
        viewModelScope.launch {
            authState.value.user?.let { user ->
                authState.setUser(user.copy(isVerified = true))
            }
        }
    }
}
