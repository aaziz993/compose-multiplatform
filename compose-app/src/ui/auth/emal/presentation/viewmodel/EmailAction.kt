package ui.auth.emal.presentation.viewmodel

public sealed interface EmailAction {
    public data class SetEmail(val value: String = "") : EmailAction
    public data object Confirm : EmailAction
}
