package ui.auth.totp.viewmodel

public sealed interface TotpAction {
    public data class SetCode(val value: String) : TotpAction
    public data object SendCode : TotpAction

    public data object Confirm : TotpAction
}
