package clib.presentation.components.picker.country

import klib.data.validator.Validator

public class CountryPickerValidator {

    public companion object {

        public fun validatePhoneNumber(number: String, countryDial: String): Boolean =
            Validator.delimitedPhone(startsWithPlus = false).validate("$countryDial$number").isEmpty()
    }
}


