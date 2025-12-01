package ui.auth.phone.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import clib.presentation.components.country.CountryCodePickerTextField
import clib.presentation.components.country.model.CountryPicker
import clib.presentation.navigation.NavigationAction
import compose_app.generated.resources.Res
import compose_app.generated.resources.country
import compose_app.generated.resources.phone
import compose_app.generated.resources.search
import data.type.primitives.string.asStringResource
import klib.data.location.country.Country
import klib.data.location.country.getCountries
import klib.data.type.primitives.string.DIGIT_PATTERN
import org.jetbrains.compose.resources.stringResource
import ui.auth.phone.presentation.viewmodel.PhoneAction
import ui.auth.phone.presentation.viewmodel.PhoneState
import ui.navigation.presentation.Phone

@Composable
public fun PhoneScreen(
    modifier: Modifier = Modifier,
    route: Phone = Phone,
    state: PhoneState = PhoneState(),
    onAction: (PhoneAction) -> Unit = {},
    country: Country = Country.getCountries().first(),
    onNavigationAction: (NavigationAction) -> Unit = {},
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(Res.string.phone),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
        )

        CountryCodePickerTextField(
            value = state.number,
            onValueChange = { countryCode, value, isValid ->
                onAction(
                    PhoneAction.SetPhone(
                        countryCode,
                        if ("${Regex.DIGIT_PATTERN}+".toRegex().matches(value)) value else state.number,
                        isValid,
                    ),
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            selectedCountry = country,
            countries = Country.getCountries().toList().map { country ->
                country.copy(name = country.toString().asStringResource { country.name })
            },
            enabled = true,
            textStyle = MaterialTheme.typography.bodyMedium,
            label = {
                Text(
                    text = stringResource(Res.string.phone), style = MaterialTheme.typography.bodyMedium,
                )
            },
            trailingIcon = {
                IconButton(onClick = { onAction(PhoneAction.SetPhone()) }) {
                    Icon(
                        imageVector = Icons.Default.Clear, contentDescription = "Clear",
                    )
                }
            },
            showError = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    onAction(PhoneAction.Confirm)
                },
            ),
            shape = RoundedCornerShape(10.dp),
            picker = CountryPicker(
                headerTitle = stringResource(Res.string.country),
                searchHint = stringResource(Res.string.search),
            ),
            showSheet = true,
        )
    }
}

@Preview
@Composable
public fun PreviewPhoneScreen(): Unit = PhoneScreen()
