package ui.auth.phonecheckup.presentation

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import clib.presentation.locale.LocalAppLocale
import compose_app.generated.resources.Res
import compose_app.generated.resources.phone
import klib.data.location.country.Country
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.auth.phonecheckup.presentation.viewmodel.PhoneCheckUpAction
import ui.auth.phonecheckup.presentation.viewmodel.PhoneCheckUpState
import ui.navigation.presentation.PhoneCheckUp

@Composable
public fun PhoneCheckUpScreen(
    modifier: Modifier = Modifier,
    route: PhoneCheckUp = PhoneCheckUp,
    state: PhoneCheckUpState = PhoneCheckUpState(),
    onAction: (PhoneCheckUpAction) -> Unit = {},
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

        var country by remember {
            mutableStateOf(Country.forCode("TJ"))
        }

        if (!LocalInspectionMode.current)
            LocalAppLocale.current.countries().firstOrNull()?.let { country = it }

        CountryCodePickerTextField(
            value = state.number,
            onValueChange = { countryCode, value, isValid ->
                onAction(PhoneCheckUpAction.SetPhone(countryCode, value, isValid))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            selectedCountry = country,
            enabled = true,
            textStyle = MaterialTheme.typography.bodyMedium,
            label = {
                Text(
                    text = stringResource(Res.string.phone), style = MaterialTheme.typography.bodyMedium,
                )
            },
            trailingIcon = {
                IconButton(onClick = { onAction(PhoneCheckUpAction.SetPhone()) }) {
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
                    onAction(PhoneCheckUpAction.Confirm)
                },
            ),
            shape = RoundedCornerShape(10.dp),
            showSheet = true,
        )
    }
}

@Preview
@Composable
public fun PreviewPhoneConfirmScreen(): Unit = PhoneCheckUpScreen()
