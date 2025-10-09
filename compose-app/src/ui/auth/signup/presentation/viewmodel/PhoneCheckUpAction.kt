package ui.auth.signup.presentation.viewmodel

public sealed interface PhoneCheckUpAction {
    public data class SetPhone(val value: String) : PhoneCheckUpAction

    public data object Confirm : PhoneCheckUpAction
}
