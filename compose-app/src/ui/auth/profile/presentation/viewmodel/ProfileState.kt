package ui.auth.profile.presentation.viewmodel

import clib.presentation.components.dialog.password.model.PasswordDialogState
import clib.presentation.components.dialog.password.model.PasswordResetDialogState
import klib.data.auth.model.User

public data class ProfileState(
    val user: User = User(),
    val editUser: Boolean = false,
    val passwordDialogState: PasswordDialogState? = null,
    val passwordResetDialogState: PasswordResetDialogState? = null,
)
