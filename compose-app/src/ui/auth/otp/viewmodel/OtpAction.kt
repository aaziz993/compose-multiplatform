package ui.auth.otp.viewmodel

public sealed interface OtpAction {
    public data class SetCode(val value: String) : OtpAction
    public data object SendCode : OtpAction

    public data object Confirm : OtpAction
}
