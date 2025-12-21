package ui.auth.profile.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Attribution
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import clib.data.type.orErrorColor
import clib.data.type.primitives.string.stringResource
import clib.presentation.components.image.avatar.Avatar
import clib.presentation.components.loading.CenterLoadingIndicator
import clib.presentation.components.textfield.AdvancedTextField
import clib.presentation.connectivity.model.Connectivity
import clib.presentation.events.alert.GlobalAlertEventController
import clib.presentation.events.alert.model.AlertEvent
import clib.presentation.navigation.NavigationAction
import compose_app.generated.resources.Res
import compose_app.generated.resources.close
import compose_app.generated.resources.edit
import compose_app.generated.resources.email
import compose_app.generated.resources.first_name
import compose_app.generated.resources.last_name
import compose_app.generated.resources.phone
import compose_app.generated.resources.save
import compose_app.generated.resources.sign_out
import compose_app.generated.resources.username
import compose_app.generated.resources.verify
import data.type.primitives.string.asStringResource
import dev.jordond.connectivity.Connectivity.Status
import klib.data.load.LoadingResult
import klib.data.load.success
import klib.data.type.collections.all
import klib.data.validator.Validator
import presentation.components.dialog.SignOutConfirmDialog
import presentation.connectivity.CircleIcon
import ui.auth.profile.presentation.viewmodel.ProfileAction
import ui.auth.profile.presentation.viewmodel.ProfileState
import ui.navigation.presentation.Profile
import ui.navigation.presentation.Verification

@Composable
public fun ProfileScreen(
    modifier: Modifier = Modifier,
    route: Profile = Profile,
    connectivityStatus: Status = Status.Disconnected,
    connectivity: Connectivity = Connectivity(),
    validator: Map<String, Validator> = emptyMap(),
    state: LoadingResult<ProfileState> = success(ProfileState()),
    onAction: (ProfileAction) -> Unit = {},
    onNavigationActions: (Array<NavigationAction>) -> Unit = {},
): Unit = when (val result = state.toSuccess()) {
    is LoadingResult.Loading -> CenterLoadingIndicator()

    is LoadingResult.Success -> ProfileScreenContent(
        modifier,
        connectivityStatus,
        connectivity,
        validator,
        result.value,
        onAction,
        onNavigationActions,
    )

    is LoadingResult.Failure -> {
        GlobalAlertEventController.sendEvent(
            AlertEvent(
                text = { Text(result.throwable.message.orEmpty()) },
            ),
        )
    }
}

@Composable
private fun ProfileScreenContent(
    @Suppress("ComposeModifierWithoutDefault") modifier: Modifier,
    connectivityStatus: Status,
    connectivity: Connectivity,
    validator: Map<String, Validator>,
    state: ProfileState,
    onAction: (ProfileAction) -> Unit,
    onNavigationActions: (Array<NavigationAction>) -> Unit,
) = Column(
    modifier = modifier,
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
) {
    Box {
        Avatar(
            user = state.user,
            modifier = Modifier.size(80.dp)
                .clip(CircleShape),
        )
        if (connectivity.isAvatarConnectivityIndicator)
            connectivityStatus.CircleIcon(
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
                contentDescription = stringResource(Res.string.edit),
                modifier = Modifier.size(14.dp),
            )
        }

        if (state.user.roles.isNotEmpty()) {
            FlowRow(
                modifier = Modifier.align(Alignment.BottomCenter),
            ) {
                state.user.roles.forEach { role ->
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
    }

    Spacer(modifier = Modifier.height(32.dp))

    val focusRequesters = remember { List(7 + state.user.attributes.size) { FocusRequester() } }
    val validations = remember { mutableStateListOf(*Array(7 + state.user.attributes.size) { false }) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
    ) {
        if (state.edit) {
            val isValidAll = validations.all()
            IconButton(
                onClick = {
                    if (isValidAll) onAction(ProfileAction.StartUpdate())
                },
                modifier = Modifier.focusRequester(focusRequesters[6]),
            ) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = stringResource(Res.string.save),
                    tint = LocalContentColor.current.orErrorColor(!isValidAll),
                )
            }
        }
        IconButton(
            onClick = {
                if (!state.edit) focusRequesters[0].requestFocus()
                onAction(ProfileAction.Edit(!state.edit))
            },
        ) {
            if (state.edit)
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(Res.string.close),
                    tint = MaterialTheme.colorScheme.error,
                )
            else Icon(Icons.Default.Edit, stringResource(Res.string.edit))
        }
    }

    Spacer(modifier = Modifier.height(32.dp))

    ProfileAttributeField(
        state.user.username.orEmpty(),
        { value -> onAction(ProfileAction.SetUsername(value)) },
        focusRequesters[0],
        focusRequesters[1],
        state.edit,
        stringResource(Res.string.username),
        Icons.Default.Person,
        validator["username"],
        { value -> validations[0] = value },
    )

    Spacer(modifier = Modifier.height(32.dp))

    ProfileAttributeField(
        state.user.firstName.orEmpty(),
        { value -> onAction(ProfileAction.SetFirstName(value)) },
        focusRequesters[1],
        focusRequesters[2],
        state.edit,
        stringResource(Res.string.first_name),
        Icons.Default.Person,
        validator["firstName"],
        { value -> validations[1] = value },
    )

    Spacer(modifier = Modifier.height(32.dp))

    ProfileAttributeField(
        state.user.lastName.orEmpty(),
        { value -> onAction(ProfileAction.SetLastName(value)) },
        focusRequesters[2],
        focusRequesters[3],
        state.edit,
        stringResource(Res.string.last_name),
        Icons.Default.Person,
        validator["lastName"],
        { value -> validations[2] = value },
    )

    Spacer(modifier = Modifier.height(32.dp))

    ProfileAttributeField(
        state.user.phone.orEmpty(),
        { value -> onAction(ProfileAction.SetPhone(value)) },
        focusRequesters[3],
        focusRequesters[4],
        state.edit,
        stringResource(Res.string.phone),
        Icons.Default.Phone,
        validator["phone"],
        { value -> validations[3] = value },
    )

    Spacer(modifier = Modifier.height(32.dp))

    ProfileAttributeField(
        state.user.email.orEmpty(),
        { value -> onAction(ProfileAction.SetEmail(value)) },
        focusRequesters[4],
        focusRequesters[5],
        state.edit,
        stringResource(Res.string.email),
        Icons.Default.Email,
        validator["email"],
        { value -> validations[4] = value },
    )

    state.user.attributes.entries.forEachIndexed { index, (key, value) ->
        Spacer(modifier = Modifier.height(32.dp))

        ProfileAttributeField(
            value.first(),
            { onAction(ProfileAction.SetAttribute(key, listOf(it))) },
            focusRequesters[5 + index],
            focusRequesters[6 + index],
            state.edit,
            key.asStringResource(),
            Icons.Default.Attribution,
            validator[key],
            { value -> validations[5 + index] = value },
        )
    }

//        CountryCodePickerTextField(
//            value = user.phone.orEmpty().removePrefix(country?.dial.orEmpty().ifNotEmpty { "+$it" }),
//            onValueChange = { _, _, _ -> },
//            modifier = Modifier.fillMaxWidth(),
//            selectedCountry = country ?: Country.getCountries().first(),
//            label = { Text(stringResource(Res.string.phone)) },
//            picker = CountryPicker(
//                headerTitle = stringResource(Res.string.country),
//                searchHint = stringResource(Res.string.search),
//            ),
//        )

    if (!state.user.roles.contains("Verified"))
        Button(
            onClick = {
                onNavigationActions(
                    arrayOf(
                        NavigationAction.Push(Verification),
                    ),
                )
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
                onAction(ProfileAction.SignOut)
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

@Composable
private fun ProfileAttributeField(
    value: String,
    onValueChange: (String) -> Unit,
    focusRequester: FocusRequester,
    nextFocusRequester: FocusRequester,
    edit: Boolean,
    label: String,
    imageVector: ImageVector,
    validator: Validator?,
    onValidation: (Boolean) -> Unit,
): Unit = AdvancedTextField(
    modifier = Modifier.fillMaxWidth().padding(8.dp).focusRequester(focusRequester),
    value = value,
    onValueChange = onValueChange,
    readOnly = !edit,
    label = { Text(label) },
    leadingIcon = { value ->
        Icon(
            imageVector = imageVector,
            contentDescription = label,
            tint = LocalContentColor.current.orErrorColor(value),
        )
    },
    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
    keyboardActions = KeyboardActions(onDone = { nextFocusRequester.requestFocus() }),
    singleLine = true,
    validator = validator,
    onValidation = { value ->
        onValidation(value.isEmpty())
        value.map { it.asStringResource() }.joinToString("\n")
    },
)

@Preview
@Composable
private fun PreviewProfileScreen(): Unit = ProfileScreen()

