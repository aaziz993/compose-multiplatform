package ui.auth.otp.viewmodel

public sealed interface OtpAction {
    public data class SetCode(val value: String) : OtpAction
    public data object ResendCode : OtpAction

    public data class Confirm(val phone: String) : OtpAction
}
