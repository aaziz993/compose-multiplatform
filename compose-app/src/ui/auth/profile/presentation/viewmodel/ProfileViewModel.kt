package ui.auth.profile.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import clib.presentation.auth.AuthState
import clib.presentation.components.dialog.password.model.PasswordDialogState
import clib.presentation.components.dialog.password.model.PasswordResetDialogState
import clib.presentation.viewmodel.ViewModel
import klib.data.auth.model.Auth
import klib.data.load.LoadingResult
import klib.data.load.success
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Provided

@KoinViewModel
public class ProfileViewModel(
    @Provided
    private val authState: AuthState,
) : ViewModel<ProfileAction>() {

    public val state: StateFlow<LoadingResult<ProfileState>>
        field = MutableStateFlow<LoadingResult<ProfileState>>(success(ProfileState(authState.value.user!!)))

    override fun action(action: ProfileAction) {
        when (action) {
            is ProfileAction.Edit -> editUser(action.value)
            is ProfileAction.SetImage -> setUserImage(action.value)
            is ProfileAction.SetUsername -> setUsername(action.value)
            is ProfileAction.SetFirstName -> setFirstName(action.value)
            is ProfileAction.SetLastName -> setLastName(action.value)
            is ProfileAction.SetPhone -> setPhone(action.value)
            is ProfileAction.SetEmail -> setEmail(action.value)
            is ProfileAction.SetAttribute -> setAttribute(action.key, action.value)
            is ProfileAction.StartUpdate -> startUpdate(action.value)
            ProfileAction.CompleteUpdate -> completeUpdate()
            is ProfileAction.StartResetPassword -> startResetPassword(action.value)
            ProfileAction.CompleteResetPassword -> completeResetPassword()
            is ProfileAction.SignOut -> signOut()
            is ProfileAction.Restore -> restore()
        }
    }

    private fun editUser(value: Boolean) = state.update {
        it.map { if (value) copy(edit = true) else copy(user = authState.value.user!!, edit = false) }
    }

    private fun setUserImage(value: String) = state.update {
        it.map { copy(user = user.copy(image = value)) }
    }

    private fun setUsername(value: String) = state.update {
        it.map { copy(user = user.copy(username = value)) }
    }

    private fun setFirstName(value: String) = state.update {
        it.map { copy(user = user.copy(firstName = value)) }
    }

    private fun setLastName(value: String) = state.update {
        it.map { copy(user = user.copy(lastName = value)) }
    }

    private fun setPhone(value: String) = state.update {
        it.map { copy(user = user.copy(phone = value)) }
    }

    private fun setEmail(value: String) = state.update {
        it.map { copy(user = user.copy(email = value)) }
    }

    private fun setAttribute(key: String, value: List<String>) = state.update {
        it.map { copy(user = user.copy(attributes = user.attributes + (key to value))) }
    }

    private fun startUpdate(value: PasswordDialogState?) = state.update {
        it.map { copy(passwordDialogState = value) }
    }

    private fun completeUpdate() {
        state.update(LoadingResult<ProfileState>::toLoading)
        viewModelScope.launch {
            authState.setUser(state.value.value!!.user)
        }
    }

    private fun startResetPassword(value: PasswordResetDialogState?) = state.update {
        it.map { copy(passwordResetDialogState = value) }
    }

    private fun completeResetPassword() {
        state.update(LoadingResult<ProfileState>::toLoading)
        viewModelScope.launch {
            state.update {
                it.map { copy(passwordResetDialogState = null) }
            }
        }
    }

    private fun signOut() {
        authState.value = Auth()
    }

    private fun restore() = state.update { it.toSuccess(false) }
}
