package clib.presentation.components.country

import io.michaelrocks.libphonenumber.kotlin.PhoneNumberUtil

public class CountryPickerValidator(private val phoneNumberUtil: PhoneNumberUtil) {

    public operator fun invoke(number: String, countryDial: String): Boolean {
        val fullNumber = "$countryDial$number"
        return try {
            val phoneNumber = phoneNumberUtil.parse(fullNumber, null)
            phoneNumberUtil.isValidNumber(phoneNumber)
        }
        catch (_: Exception) {
            false
        }
    }
}


