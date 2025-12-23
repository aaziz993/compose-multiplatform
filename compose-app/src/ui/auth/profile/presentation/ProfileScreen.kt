package ui.auth.profile.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Attribution
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
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
import clib.presentation.components.country.CountryCodePickerTextField
import clib.presentation.components.country.model.CountryPicker
import clib.presentation.components.image.avatar.Avatar
import clib.presentation.components.loading.CenterLoadingIndicator
import clib.presentation.components.textfield.AdvancedTextField
import clib.presentation.connectivity.model.Connectivity
import clib.presentation.events.alert.GlobalAlertEventController
import clib.presentation.events.alert.model.AlertEvent
import clib.presentation.navigation.NavigationAction
import compose_app.generated.resources.Res
import compose_app.generated.resources.avatar
import compose_app.generated.resources.close
import compose_app.generated.resources.country
import compose_app.generated.resources.verified
import compose_app.generated.resources.edit
import compose_app.generated.resources.email
import compose_app.generated.resources.first_name
import compose_app.generated.resources.last_name
import compose_app.generated.resources.phone
import compose_app.generated.resources.save
import compose_app.generated.resources.search
import compose_app.generated.resources.sign_out
import compose_app.generated.resources.username
import compose_app.generated.resources.verify
import data.type.primitives.string.asStringResource
import dev.jordond.connectivity.Connectivity.Status
import klib.data.load.LoadingResult
import klib.data.load.success
import klib.data.location.Phone
import klib.data.location.country.Country
import klib.data.location.country.getCountries
import klib.data.location.toPhoneOrNull
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
                isError = true,
                dismissRequestAction = {
                    GlobalAlertEventController.sendEvent(null)
                    onAction(ProfileAction.Restore)
                },
                dismissAction = {
                    GlobalAlertEventController.sendEvent(null)
                    onAction(ProfileAction.Restore)
                },
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
): Unit = Box(
    modifier = modifier,
    contentAlignment = Alignment.Center,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box {
            Avatar(
                user = state.user,
                modifier = Modifier.size(80.dp)
                    .clip(CircleShape),
                contentDescription = stringResource(Res.string.avatar),
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

            if (state.user.isVerified)
                Icon(
                    imageVector = Icons.Default.VerifiedUser,
                    contentDescription = stringResource(Res.string.verified),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .offset((-20).dp, 5.dp)
                        .size(24.dp),
                )

            IconButton(
                onClick = {},
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(20.dp, 5.dp)
                    .size(24.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(Res.string.edit),
                    modifier = Modifier.size(14.dp),
                )
            }
        }

        if (state.user.roles.isNotEmpty()) {
            FlowRow {
                state.user.roles.forEach { role ->
                    AssistChip(
                        onClick = {},
                        label = {
                            Text(role, style = MaterialTheme.typography.labelSmall)
                        },
                        modifier = Modifier.defaultMinSize(minHeight = 32.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            labelColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        ),
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        val focusRequesters = remember(state.user.attributes.size) {
            List(7 + state.user.attributes.size) { FocusRequester() }
        }

        LaunchedEffect(state.edit) {
            if (state.edit) focusRequesters[0].requestFocus()
        }

        val validations = remember(state.user.attributes.size) {
            mutableStateListOf(*Array(7 + state.user.attributes.size) { true })
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            if (state.edit) {
                val isValidAll by remember(validations) { derivedStateOf(validations::all) }
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

        Spacer(modifier = Modifier.height(16.dp))

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

        Spacer(modifier = Modifier.height(16.dp))

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

        Spacer(modifier = Modifier.height(16.dp))

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

        Spacer(modifier = Modifier.height(16.dp))

        var phone by remember(state.user.phone) {
            mutableStateOf(state.user.phone?.toPhoneOrNull())
        }

        phone?.let {
            val selectedCountry = remember {
                Country.getCountries().first { country -> country.dial == it.dial }
            }
            CountryCodePickerTextField(
                value = it.number,
                onValueChange = { countryCode, value, _ ->
                    phone = Phone(countryCode, value)
                    onAction(ProfileAction.SetPhone(phone.toString()))
                },
                modifier = Modifier.fillMaxWidth(),
                selectedCountry = selectedCountry,
                readOnly = !state.edit,
                label = { Text(stringResource(Res.string.phone)) },
                picker = CountryPicker(
                    headerTitle = stringResource(Res.string.country),
                    searchHint = stringResource(Res.string.search),
                ),
            )
        } ?: ProfileAttributeField(
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

        Spacer(modifier = Modifier.height(16.dp))

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
            Spacer(modifier = Modifier.height(16.dp))

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

        if (!state.user.isVerified)
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

        var singOutDialog by remember { mutableStateOf(false) }
        if (singOutDialog)
            SignOutConfirmDialog(
                {
                    singOutDialog = false
                },
                {
                    onAction(ProfileAction.SignOut)
                },
            )

        Button(
            onClick = {
                singOutDialog = true
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = stringResource(Res.string.sign_out))
        }
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
    onValidation = {
        onValidation(it.isEmpty())
        it.map { it.asStringResource() }.joinToString("\n")
    },
)

@Preview
@Composable
private fun PreviewProfileScreen(): Unit = ProfileScreen()

