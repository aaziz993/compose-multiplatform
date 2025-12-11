package ui.auth.hotp.viewmodel

public sealed interface HotpAction {
    public data class SetCode(val value: String) : HotpAction

    public data object SendNewCode : HotpAction

    public data object Confirm : HotpAction
}
