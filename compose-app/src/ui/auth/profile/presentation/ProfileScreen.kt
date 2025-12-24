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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Attribution
import androidx.compose.material.icons.filled.CameraEnhance
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
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
import androidx.compose.runtime.key
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import clib.data.type.orErrorColor
import clib.data.type.primitives.string.stringResource
import clib.presentation.components.country.CountryCodePickerTextField
import clib.presentation.components.country.model.CountryPicker
import clib.presentation.components.image.avatar.Avatar
import clib.presentation.components.loading.CenterLoadingIndicator
import clib.presentation.components.textfield.TextField
import clib.presentation.connectivity.model.Connectivity
import clib.presentation.events.alert.GlobalAlertEventController
import clib.presentation.events.alert.model.AlertEvent
import clib.presentation.navigation.NavigationAction
import compose_app.generated.resources.Res
import compose_app.generated.resources.add
import compose_app.generated.resources.avatar
import compose_app.generated.resources.camera
import compose_app.generated.resources.close
import compose_app.generated.resources.country
import compose_app.generated.resources.edit
import compose_app.generated.resources.email
import compose_app.generated.resources.first_name
import compose_app.generated.resources.gallery
import compose_app.generated.resources.key
import compose_app.generated.resources.last_name
import compose_app.generated.resources.phone
import compose_app.generated.resources.remove
import compose_app.generated.resources.save
import compose_app.generated.resources.search
import compose_app.generated.resources.sign_out
import compose_app.generated.resources.username
import compose_app.generated.resources.value
import compose_app.generated.resources.verified
import compose_app.generated.resources.verify
import data.type.primitives.string.asStringResource
import dev.jordond.connectivity.Connectivity.Status
import io.github.ismoy.imagepickerkmp.domain.config.ImagePickerConfig
import io.github.ismoy.imagepickerkmp.presentation.ui.components.GalleryPickerLauncher
import io.github.ismoy.imagepickerkmp.presentation.ui.components.ImagePickerLauncher
import klib.data.auth.model.User
import klib.data.load.LoadingResult
import klib.data.load.success
import klib.data.location.Phone
import klib.data.location.country.Country
import klib.data.location.country.getCountries
import klib.data.location.toPhoneOrNull
import klib.data.type.collections.all
import klib.data.type.primitives.string.LINE_SEPARATOR
import klib.data.validator.Validator
import presentation.components.dialog.SignOutConfirmDialog
import presentation.components.tooltipbox.PlainTooltipBox
import presentation.connectivity.CircleIcon
import presentation.connectivity.stringResource
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
        var showCamera by remember { mutableStateOf(false) }
        if (showCamera) {
            ImagePickerLauncher(
                config = ImagePickerConfig(
                    onPhotoCaptured = { result ->
                        onAction(ProfileAction.SetImageUrl(result.uri))
                        showCamera = false
                    },
                    onError = { showCamera = false },
                    onDismiss = { showCamera = false },
                ),
            )
        }

        var showGallery by remember { mutableStateOf(false) }
        if (showGallery) {
            GalleryPickerLauncher(
                onPhotosSelected = { result ->
                    onAction(ProfileAction.SetImageUrl(result.single().uri))
                    showGallery = false
                },
                onError = { showGallery = false },
                onDismiss = { showGallery = false },
                allowMultiple = false,
            )
        }

        Box {
            Avatar(
                user = state.user,
                modifier = Modifier.size(80.dp)
                    .clip(CircleShape),
                contentDescription = stringResource(Res.string.avatar),
            )
            if (connectivity.isAvatarConnectivityIndicator)
                PlainTooltipBox(tooltip = connectivityStatus.stringResource()) {
                    connectivityStatus.CircleIcon(
                        Modifier
                            .align(Alignment.TopStart)
                            .size(14.dp),
                        Modifier
                            .align(Alignment.TopStart)
                            .size(14.dp),
                    )
                }

            if (state.edit) {
                IconButton(
                    onClick = {
                        onAction(ProfileAction.SetImageUrl(null))
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(20.dp, 5.dp)
                        .size(24.dp),
                ) {
                    PlainTooltipBox(tooltip = stringResource(Res.string.remove)) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = stringResource(Res.string.remove),
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.error,
                        )
                    }
                }

                IconButton(
                    onClick = {
                        showGallery = true
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(20.dp, 5.dp)
                        .size(24.dp),
                ) {
                    PlainTooltipBox(tooltip = stringResource(Res.string.gallery)) {
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = stringResource(Res.string.gallery),
                            modifier = Modifier.size(14.dp),
                        )
                    }
                }

                IconButton(
                    onClick = {
                        showCamera = true
                    },
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .offset((-20).dp, 5.dp)
                        .size(24.dp),
                ) {
                    PlainTooltipBox(tooltip = stringResource(Res.string.camera)) {
                        Icon(
                            imageVector = Icons.Default.CameraEnhance,
                            contentDescription = stringResource(Res.string.camera),
                            modifier = Modifier.size(14.dp),
                        )
                    }
                }
            }

            if (state.user.isVerified)
                PlainTooltipBox(tooltip = stringResource(Res.string.verified)) {
                    Icon(
                        imageVector = Icons.Default.VerifiedUser,
                        contentDescription = stringResource(Res.string.verified),
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .offset(y = 5.dp)
                            .size(14.dp),
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

        val focusRequesters = remember(state.user) {
            List(6 + state.user.attributes.size) { FocusRequester() }
        }

        LaunchedEffect(state.edit) {
            focusRequesters[0].requestFocus()
        }

        val validations = remember(state.user) {
            mutableStateListOf(*Array(6 + state.user.attributes.size) { true })
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
                    modifier = Modifier.focusRequester(focusRequesters[5 + state.user.attributes.size]),
                ) {
                    PlainTooltipBox(tooltip = stringResource(Res.string.save)) {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = stringResource(Res.string.save),
                            tint = LocalContentColor.current.orErrorColor(!isValidAll),
                        )
                    }
                }
            }
            IconButton(
                onClick = {
                    onAction(ProfileAction.Edit(!state.edit))
                },
            ) {
                if (state.edit)
                    PlainTooltipBox(tooltip = stringResource(Res.string.close)) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(Res.string.close),
                            tint = MaterialTheme.colorScheme.error,
                        )
                    }
                else PlainTooltipBox(tooltip = stringResource(Res.string.edit)) {
                    Icon(Icons.Default.Edit, stringResource(Res.string.edit))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        ProfileTextField(
            value = state.user.username.orEmpty(),
            onValueChange = { value -> onAction(ProfileAction.SetUsername(value)) },
            focusRequester = focusRequesters[0],
            nextFocusRequester = focusRequesters[1],
            edit = state.edit,
            label = stringResource(Res.string.username),
            imageVector = Icons.Default.Person,
            validator = validator[User::username.name],
        ) { value -> validations[0] = value }

        Spacer(modifier = Modifier.height(16.dp))

        ProfileTextField(
            value = state.user.firstName.orEmpty(),
            onValueChange = { value -> onAction(ProfileAction.SetFirstName(value)) },
            focusRequester = focusRequesters[1],
            nextFocusRequester = focusRequesters[2],
            edit = state.edit,
            label = stringResource(Res.string.first_name),
            imageVector = Icons.Default.Person,
            validator = validator[User::firstName.name],
        ) { value -> validations[1] = value }

        Spacer(modifier = Modifier.height(16.dp))

        ProfileTextField(
            value = state.user.lastName.orEmpty(),
            onValueChange = { value -> onAction(ProfileAction.SetLastName(value)) },
            focusRequester = focusRequesters[2],
            nextFocusRequester = focusRequesters[3],
            edit = state.edit,
            label = stringResource(Res.string.last_name),
            imageVector = Icons.Default.Person,
            validator = validator[User::lastName.name],
        ) { value -> validations[2] = value }

        Spacer(modifier = Modifier.height(16.dp))

        var phone by remember(state.user.phone) {
            mutableStateOf(state.user.phone?.toPhoneOrNull())
        }

        phone?.let {
            key(state.edit) {
                val selectedCountry = remember {
                    Country.getCountries().first { country -> country.dial == it.dial }
                }
                CountryCodePickerTextField(
                    value = it.number,
                    onValueChange = { dial, number, _ ->
                        phone = Phone(dial, number)
                        onAction(ProfileAction.SetPhone(phone.toString()))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    selectedCountry = selectedCountry,
                    countries = Country.getCountries().toList().map { country ->
                        country.copy(name = "country_$country".asStringResource { country.name })
                    },
                    readOnly = !state.edit,
                    label = {
                        Text(text = stringResource(Res.string.phone), overflow = TextOverflow.Clip, maxLines = 1)
                    },
                    picker = CountryPicker(
                        headerTitle = stringResource(Res.string.country),
                        searchHint = stringResource(Res.string.search),
                    ),
                )
            }
        } ?: ProfileTextField(
            value = state.user.phone.orEmpty(),
            onValueChange = { value -> onAction(ProfileAction.SetPhone(value)) },
            focusRequester = focusRequesters[3],
            nextFocusRequester = focusRequesters[4],
            edit = state.edit,
            label = stringResource(Res.string.phone),
            imageVector = Icons.Default.Phone,
            validator = validator[User::phone.name],
        ) { value -> validations[3] = value }


        Spacer(modifier = Modifier.height(16.dp))

        ProfileTextField(
            value = state.user.email.orEmpty(),
            onValueChange = { value -> onAction(ProfileAction.SetEmail(value)) },
            focusRequester = focusRequesters[4],
            nextFocusRequester = focusRequesters[5],
            edit = state.edit,
            label = stringResource(Res.string.email),
            imageVector = Icons.Default.Email,
            validator = validator[User::email.name],
        ) { value -> validations[4] = value }

        state.user.attributes.entries.forEachIndexed { index, (key, value) ->
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ProfileTextField(
                    modifier = Modifier.weight(1f).padding(8.dp),
                    value = value.first(),
                    onValueChange = { onAction(ProfileAction.SetAttribute(key, listOf(it))) },
                    focusRequester = focusRequesters[5 + index],
                    nextFocusRequester = focusRequesters[6 + index],
                    edit = state.edit,
                    label = key.asStringResource(),
                    imageVector = Icons.Default.Attribution,
                    validator = validator[key],
                ) { value -> validations[5] = value }

                if (state.edit) {
                    IconButton(
                        onClick = {
                            onAction(ProfileAction.RemoveAttribute(key))
                        },
                        modifier = Modifier.weight(1f),
                    ) {
                        Icon(Icons.Default.Remove, stringResource(Res.string.remove))
                    }
                }
            }
        }

        if (state.edit) {
            Spacer(modifier = Modifier.height(16.dp))

            var key by remember { mutableStateOf("") }
            var value by remember { mutableStateOf("") }

            TextField(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                value = key,
                onValueChange = { value -> key = value },
                label = {
                    Text(text = stringResource(Res.string.key), overflow = TextOverflow.Clip, maxLines = 1)
                },
                placeholder = {
                    Text(text = stringResource(Res.string.key), overflow = TextOverflow.Clip, maxLines = 1)
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Key,
                        stringResource(Res.string.key),
                    )
                },
                singleLine = true,
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                value = value,
                onValueChange = { value = it },
                label = {
                    Text(text = stringResource(Res.string.value), overflow = TextOverflow.Clip, maxLines = 1)
                },
                placeholder = {
                    Text(text = stringResource(Res.string.value), overflow = TextOverflow.Clip, maxLines = 1)
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Attribution,
                        stringResource(Res.string.value),
                    )
                },
                validator = validator[key],
                onValidation = { value ->
                    validations[5 + state.user.attributes.size] = value.isEmpty()
                    value.map { it.asStringResource() }.joinToString(String.LINE_SEPARATOR)
                },
            )

            Spacer(modifier = Modifier.height(16.dp))

            IconButton(
                onClick = {
                    onAction(ProfileAction.SetAttribute(key, listOf(value)))
                },
            ) {
                PlainTooltipBox(tooltip = stringResource(Res.string.add)) {
                    Icon(Icons.Default.Add, stringResource(Res.string.add))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

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
                Text(text = stringResource(Res.string.verify), overflow = TextOverflow.Clip, maxLines = 1)
            }

        Spacer(modifier = Modifier.height(16.dp))

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
            Text(text = stringResource(Res.string.sign_out), overflow = TextOverflow.Clip, maxLines = 1)
        }
    }
}

@Suppress("ComposeParameterOrder")
@Composable
private fun ProfileTextField(
    modifier: Modifier = Modifier.fillMaxWidth().padding(8.dp),
    value: String,
    onValueChange: (String) -> Unit,
    focusRequester: FocusRequester,
    nextFocusRequester: FocusRequester,
    edit: Boolean,
    label: String,
    imageVector: ImageVector,
    validator: Validator?,
    onValidation: (Boolean) -> Unit,
): Unit = TextField(
    modifier = modifier.focusRequester(focusRequester),
    value = value,
    onValueChange = onValueChange,
    readOnly = !edit,
    label = { Text(text = label, overflow = TextOverflow.Clip, maxLines = 1) },
    placeholder = { Text(text = label, overflow = TextOverflow.Clip, maxLines = 1) },
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
        it.map { it.asStringResource() }.joinToString(String.LINE_SEPARATOR)
    },
)

@Preview
@Composable
private fun PreviewProfileScreen(): Unit = ProfileScreen()

