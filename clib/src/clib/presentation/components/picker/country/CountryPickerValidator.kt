package clib.presentation.components.picker.country

import klib.data.validator.Validator

public object CountryPickerValidator {

    public fun validatePhoneNumber(number: String, countryDial: String): Boolean =
        Validator.delimitedPhone(startsWithPlus = false).validate("$countryDial$number").isEmpty()
}


