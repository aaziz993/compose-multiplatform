package clib.presentation.components.country

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import clib.presentation.components.country.model.CountryPicker
import clib.presentation.components.country.model.CountryView
import io.michaelrocks.libphonenumber.kotlin.PhoneNumberUtil
import io.michaelrocks.libphonenumber.kotlin.metadata.defaultMetadataLoader
import klib.data.location.country.Country
import klib.data.location.country.getCountries
import androidx.compose.ui.tooling.preview.Preview

@Composable
public fun CountryCodePickerTextField(
    value: String,
    onValueChange: (countryCode: String, value: String, isValid: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    selectedCountry: Country = Country.getCountries().first(),
    countries: List<Country> = Country.getCountries().toList(),
    enabled: Boolean = true,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    showError: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    shape: Shape = RoundedCornerShape(10.dp),
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
    view: CountryView = CountryView(),
    picker: CountryPicker = CountryPicker(),
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    showSheet: Boolean = false,
    itemPadding: Int = 10
) {

    var country by remember {
        mutableStateOf(selectedCountry)
    }

    val phoneNumberUtil: PhoneNumberUtil by remember {
        mutableStateOf(PhoneNumberUtil.createInstance(defaultMetadataLoader()))
    }

    val validatePhoneNumber = CountryPickerValidator(phoneNumberUtil)

    var isNumberValid: Boolean by rememberSaveable(country, value) {
        mutableStateOf(
            validatePhoneNumber(
                number = value, countryDial = country.dial.orEmpty(),
            ),
        )
    }

    TextField(
        value = value,
        onValueChange = {
            isNumberValid = validatePhoneNumber(
                number = it, countryDial = country.dial.orEmpty(),
            )
            onValueChange(country.dial.orEmpty(), it, isNumberValid)
        },
        modifier = modifier,
        textStyle = textStyle,
        singleLine = true,
        shape = shape,
        label = label,
        placeholder = placeholder,
        leadingIcon = {
            CountryCodePicker(
                selectedCountry = country,
                countries = countries,
                onCountrySelected = {
                    country = it
                    isNumberValid = validatePhoneNumber(
                        number = value, countryDial = it.dial.orEmpty(),
                    )
                    onValueChange(it.dial.orEmpty(), value, isNumberValid)
                },
                view = view,
                picker = picker,
                backgroundColor = backgroundColor,
                textStyle = textStyle,
                showSheet = showSheet,
                itemPadding = itemPadding,
            )

        },
        trailingIcon = trailingIcon,
        isError = !isNumberValid && value.isNotEmpty() && showError,
        visualTransformation = CountryPickerTransformer(phoneNumberUtil, country.toString()),
        enabled = enabled,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        colors = colors,
    )
}

@Preview(showBackground = true)
@Composable
public fun CountryCodePickerTextFieldPreview() {
    var value by remember { mutableStateOf("") }

    CountryCodePickerTextField(
        value = value,
        onValueChange = { _, number, _ -> value = number },
    )
}
