package ui.auth.profile.presentation.viewmodel

import clib.presentation.auth.AuthState
import clib.presentation.components.dialog.password.model.PasswordDialogState
import clib.presentation.viewmodel.ViewModel
import klib.data.auth.model.Auth
import klib.data.load.LoadingResult
import klib.data.load.success
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Provided

@KoinViewModel
public class ProfileViewModel(
    @Provided
    private val authState: AuthState,
) : ViewModel<ProfileAction>() {

    public val state: MutableStateFlow<LoadingResult<ProfileState>>
        field = MutableStateFlow(success(ProfileState(authState.value.user!!)))

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
            ProfileAction.CompleteUpdate -> {}
            is ProfileAction.StartResetPassword -> {}
            ProfileAction.CompleteResetPassword -> completeUpdate()
            is ProfileAction.SignOut -> signOut()
        }
    }

    private fun editUser(value: Boolean) = state.update {
        it.map { if (value) copy(editUser = true) else copy(user = authState.value.user!!, editUser = false) }
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
    }

    private fun signOut() {
        authState.value = Auth()
    }
}
