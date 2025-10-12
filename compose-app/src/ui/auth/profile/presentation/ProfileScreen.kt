package ui.auth.profile.presentation

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import clib.presentation.auth.LocalAppAuth
import clib.presentation.auth.stateholder.AuthAction
import clib.presentation.components.image.avatar.Avatar
import clib.presentation.components.navigation.viewmodel.NavigationAction
import clib.presentation.components.picker.country.CountryCodePickerTextField
import clib.presentation.components.textfield.AdvancedTextField
import compose_app.generated.resources.Res
import compose_app.generated.resources.sign_out
import compose_app.generated.resources.verify
import klib.data.type.auth.model.Auth
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.navigation.presentation.Profile
import ui.navigation.presentation.Verification

@Composable
public fun ProfileScreen(
    modifier: Modifier = Modifier,
    route: Profile = Profile,
    onAuthAction: (AuthAction) -> Unit = {},
    onNavigationAction: (NavigationAction) -> Unit = {},
): Unit = Column(
    modifier = modifier,
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
) {
    with(LocalAppAuth.current.user!!) {
        Avatar(
            user = this,
            modifier = Modifier.size(80.dp)
                .clip(CircleShape)
                .border(
                    0.dp,
                    MaterialTheme.colorScheme.outlineVariant,
                    CircleShape,
                ),
        ) {
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
            value = username.orEmpty(),
            onValueChange = { },
            label = { Text("Username") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(32.dp))

        AdvancedTextField(
            value = email.orEmpty(),
            onValueChange = { },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(32.dp))

        CountryCodePickerTextField(
            value = phone.orEmpty(),
            onValueChange = { _, _, _ -> },
            label = { Text("Phone") },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(32.dp))

        if (roles.isNotEmpty()) {
            FlowRow {
                roles.forEach { role ->
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

        if (attributes.isNotEmpty()) {
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
                    attributes.forEach { (key, values) ->
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

        if (LocalAppAuth.current.user?.roles?.contains("Verified") == false)
            Button(
                onClick = {
                    onNavigationAction(NavigationAction.TypeNavigation.Navigate(Verification))
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = stringResource(Res.string.verify))
            }

        Button(
            onClick = {
                onAuthAction(AuthAction.SetAuth(Auth()))
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = stringResource(Res.string.sign_out))
        }
    }
}

@Preview
@Composable
public fun PreviewProfileScreen(): Unit = ProfileScreen()
