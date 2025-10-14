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
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import clib.presentation.components.navigation.viewmodel.NavigationAction
import clib.presentation.components.picker.country.CountryCodePickerTextField
import clib.presentation.components.picker.country.mode.CountryPicker
import compose_app.generated.resources.Res
import compose_app.generated.resources.allStringResources
import compose_app.generated.resources.language
import compose_app.generated.resources.phone
import compose_app.generated.resources.search
import klib.data.location.country.Country
import klib.data.location.country.current
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.auth.phone.presentation.viewmodel.PhoneAction
import ui.auth.phone.presentation.viewmodel.PhoneState
import ui.navigation.presentation.Phone
import clib.data.type.primitives.string.stringResource
import klib.data.location.country.getCountries

@Composable
public fun PhoneScreen(
    modifier: Modifier = Modifier,
    route: Phone = Phone,
    state: PhoneState = PhoneState(),
    onAction: (PhoneAction) -> Unit = {},
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

        val country = (if (!LocalInspectionMode.current) Country.current else null) ?: Country.forCode("TJ")

        CountryCodePickerTextField(
            value = state.number,
            onValueChange = { countryCode, value, isValid ->
                onAction(PhoneAction.SetPhone(countryCode, value, isValid))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            selectedCountry = country.copy(name = stringResource(country.name, Res.allStringResources)),
            countries = Country.getCountries().toList().map { country ->
                country.copy(name = stringResource(country.name, Res.allStringResources))
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
            showSheet = true,
            picker = CountryPicker(
                headerTitle = stringResource(Res.string.language),
                searchHint = stringResource(Res.string.search),
            ),
        )
    }
}

@Preview
@Composable
public fun PreviewPhoneScreen(): Unit = PhoneScreen()
