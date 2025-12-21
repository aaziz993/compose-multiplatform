package ui.auth.profile.presentation.viewmodel

import clib.presentation.components.dialog.password.model.PasswordDialogState
import clib.presentation.components.dialog.password.model.PasswordResetDialogState

public sealed interface ProfileAction {
    public data class Edit(val value: Boolean) : ProfileAction
    public data class SetImage(val value: String) : ProfileAction
    public data class SetUsername(val value: String) : ProfileAction
    public data class SetFirstName(val value: String) : ProfileAction
    public data class SetLastName(val value: String) : ProfileAction
    public data class SetPhone(val value: String) : ProfileAction
    public data class SetEmail(val value: String) : ProfileAction
    public data class SetAttribute(val key: String, val value: List<String>) : ProfileAction
    public data class StartUpdate(val value: PasswordDialogState? = PasswordDialogState()) : ProfileAction
    public data object CompleteUpdate : ProfileAction
    public data class StartResetPassword(val value: PasswordResetDialogState? = PasswordResetDialogState()) : ProfileAction
    public data object CompleteResetPassword : ProfileAction
    public data object SignOut : ProfileAction
}
