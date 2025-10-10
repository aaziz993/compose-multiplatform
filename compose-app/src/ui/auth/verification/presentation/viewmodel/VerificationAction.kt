package ui.auth.verification.presentation.viewmodel

public sealed interface VerificationAction {
    public data class SetIdImage(val value: String) : VerificationAction
    public data class SetUserImage(val value: String) : VerificationAction
    public data object Confirm : VerificationAction
}
