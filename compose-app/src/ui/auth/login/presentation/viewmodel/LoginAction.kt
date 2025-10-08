package ui.auth.login.presentation.viewmodel

public sealed interface LoginAction {
    public data class SetUsername(val value: String) : LoginAction
    public data class SetPassword(val value: String) : LoginAction
    public data class ShowPassword(val value: Boolean) : LoginAction
    public data object Login : LoginAction
}
