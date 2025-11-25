package ui.auth.profile.presentation

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
import clib.presentation.components.image.avatar.Avatar
import clib.presentation.navigation.NavigationAction
import clib.presentation.components.country.CountryCodePickerTextField
import clib.presentation.components.textfield.AdvancedTextField
import compose_app.generated.resources.Res
import compose_app.generated.resources.sign_out
import compose_app.generated.resources.verify
import klib.data.type.auth.model.Auth
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.tooling.preview.Preview
import clib.presentation.components.country.model.CountryPicker
import compose_app.generated.resources.country
import compose_app.generated.resources.search
import ui.navigation.presentation.Profile
import ui.navigation.presentation.Verification

@Composable
public fun ProfileScreen(
    modifier: Modifier = Modifier,
    route: Profile = Profile,
    auth: Auth = Auth(),
    onAuthChange: (Auth) -> Unit = {},
    onNavigationAction: (NavigationAction) -> Unit = {},
): Unit = Column(
    modifier = modifier,
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
) {
    auth.user?.let { user ->
        Avatar(
            user = user,
            modifier = Modifier.size(80.dp)
                .clip(CircleShape),
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
            value = user.username.orEmpty(),
            onValueChange = { },
            label = { Text("Username") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(32.dp))

        AdvancedTextField(
            value = user.email.orEmpty(),
            onValueChange = { },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(32.dp))

        CountryCodePickerTextField(
            value = user.phone.orEmpty(),
            onValueChange = { _, _, _ -> },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Phone") },
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

        Button(
            onClick = {
                onAuthChange(Auth())
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
