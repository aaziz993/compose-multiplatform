package ui.auth.login.presentation.viewmodel

public sealed interface LoginAction {
    public data class SetPinCode(val value: String) : LoginAction
    public data class ShowPinCode(val value: Boolean) : LoginAction
    public data object Login : LoginAction
}
