package ui.auth.pincode.viewmodel

public sealed interface PinCodeAction {
    public data class SetPinCode(val value: String) : PinCodeAction
    public data class RepeatPinCode(val value: String) : PinCodeAction

    public data object Confirm : PinCodeAction
}
