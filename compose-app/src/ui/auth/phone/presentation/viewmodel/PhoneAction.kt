package ui.auth.phone.presentation.viewmodel

public sealed interface PhoneAction {
    public data class SetPhone(val countryCode: String = "", val number: String = "", val isValid: Boolean = false) : PhoneAction

    public data object Confirm : PhoneAction
}
