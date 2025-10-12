package ui.auth.phonecheckup.presentation.viewmodel

public sealed interface PhoneCheckUpAction {
    public data class SetPhone(val countryCode: String = "", val number: String = "", val isValid: Boolean = false) : PhoneCheckUpAction

    public data object Confirm : PhoneCheckUpAction
}
