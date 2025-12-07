package ui.auth.profile.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import clib.presentation.components.Components
import clib.presentation.components.country.CountryCodePickerTextField
import clib.presentation.components.country.model.CountryPicker
import clib.presentation.components.image.avatar.Avatar
import clib.presentation.components.textfield.AdvancedTextField
import clib.presentation.navigation.NavigationAction
import compose_app.generated.resources.Res
import compose_app.generated.resources.country
import compose_app.generated.resources.email
import compose_app.generated.resources.phone
import compose_app.generated.resources.search
import compose_app.generated.resources.sign_out
import compose_app.generated.resources.username
import compose_app.generated.resources.verify
import dev.jordond.connectivity.Connectivity.Status
import klib.data.auth.model.Auth
import klib.data.location.country.Country
import klib.data.location.country.getCountries
import klib.data.type.primitives.string.ifNotEmpty
import org.jetbrains.compose.resources.stringResource
import presentation.components.dialog.SignOutConfirmDialog
import presentation.connectivity.ConnectivityIcon
import ui.navigation.presentation.Profile
import ui.navigation.presentation.Verification

@Composable
public fun ProfileScreen(
    modifier: Modifier = Modifier,
    route: Profile = Profile,
    connectivity: Status = Status.Disconnected,
    components: Components = Components(),
    auth: Auth = Auth(),
    onAuthChange: (Auth) -> Unit = {},
    onNavigationAction: (NavigationAction) -> Unit = {},
): Unit = Column(
    modifier = modifier,
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
) {
    auth.user?.let { user ->
        Box {
            Avatar(
                user = user,
                modifier = Modifier.size(80.dp)
                    .clip(CircleShape),
            )
            if (components.connectivity.isAvatarConnectivityIndicator)
                connectivity.ConnectivityIcon(
                    Modifier
                        .align(Alignment.TopEnd)
                        .size(14.dp),
                    Modifier
                        .align(Alignment.TopEnd)
                        .size(14.dp),
                )
            IconButton(
                onClick = {},
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(20.dp, 5.dp)
                    .size(24.dp),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "Edit avatar",
                    modifier = Modifier.size(14.dp),
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        AdvancedTextField(
            value = user.username.orEmpty(),
            onValueChange = { },
            label = { Text(stringResource(Res.string.username)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(32.dp))

        AdvancedTextField(
            value = user.email.orEmpty(),
            onValueChange = { },
            label = { Text(stringResource(Res.string.email)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(32.dp))

        val country by remember(user.phone) {
            derivedStateOf {
                Country.getCountries().filter { country ->
                    user.phone.orEmpty().startsWith("+${country.dial}")
                }.maxByOrNull { country -> country.dial!!.length }
            }
        }

        CountryCodePickerTextField(
            value = user.phone.orEmpty().removePrefix(country?.dial.orEmpty().ifNotEmpty { "+$it" }),
            onValueChange = { _, _, _ -> },
            modifier = Modifier.fillMaxWidth(),
            selectedCountry = country ?: Country.getCountries().first(),
            label = { Text(stringResource(Res.string.phone)) },
            picker = CountryPicker(
                headerTitle = stringResource(Res.string.country),
                searchHint = stringResource(Res.string.search),
            ),
        )

        Spacer(modifier = Modifier.height(32.dp))

        if (user.roles.isNotEmpty()) {
            FlowRow {
                user.roles.forEach { role ->
                    AssistChip(
                        onClick = {},
                        label = { Text(role, style = MaterialTheme.typography.labelSmall) },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            labelColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        ),
                        shape = RoundedCornerShape(12.dp),
                        border = null,
                        modifier = Modifier.defaultMinSize(minHeight = 32.dp),
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (user.attributes.isNotEmpty()) {
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp),
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "Attributes",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
                    )
                    user.attributes.forEach { (key, values) ->
                        Column {
                            Text(
                                key,
                                style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                            )
                            Text(values.joinToString(", "), style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }

        if (auth.user?.roles?.contains("Verified") == false)
            Button(
                onClick = {
                    onNavigationAction(NavigationAction.Push(Verification))
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = stringResource(Res.string.verify))
            }

        var singOutConfirmDialog by remember { mutableStateOf(false) }
        if (singOutConfirmDialog)
            SignOutConfirmDialog(
                {
                    singOutConfirmDialog = false
                },
                {
                    onAuthChange(Auth())
                },
            )

        Button(
            onClick = {
                singOutConfirmDialog = true
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = stringResource(Res.string.sign_out))
        }
    }
}

@Preview
@Composable
private fun PreviewProfileScreen(): Unit = ProfileScreen()


