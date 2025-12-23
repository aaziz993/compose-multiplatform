package ui.auth.verification.presentation.viewmodel

public sealed interface VerificationAction {
    public data class SetIdUri(val value: String) : VerificationAction
    public data class SetUserUri(val value: String) : VerificationAction
    public data object Confirm : VerificationAction
}
