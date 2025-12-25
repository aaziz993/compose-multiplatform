package ui.auth.login.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import clib.data.auth.imageVector
import clib.data.auth.oauth.imageVector
import clib.data.type.orErrorColor
import clib.data.type.primitives.string.stringResource
import clib.presentation.components.textfield.TextField
import clib.presentation.navigation.NavigationAction
import compose.icons.SimpleIcons
import compose.icons.simpleicons.Apple
import compose.icons.simpleicons.Google
import compose_app.generated.resources.Res
import compose_app.generated.resources.apple
import compose_app.generated.resources.clear
import compose_app.generated.resources.email
import compose_app.generated.resources.google
import compose_app.generated.resources.login
import compose_app.generated.resources.password
import compose_app.generated.resources.phone
import compose_app.generated.resources.remember
import compose_app.generated.resources.reset_password
import compose_app.generated.resources.username
import data.type.primitives.string.asStringResource
import klib.data.config.auth.AuthConfig
import presentation.components.tooltipbox.PlainTooltipBox
import ui.auth.login.presentation.viewmodel.LoginAction
import ui.auth.login.presentation.viewmodel.LoginState
import ui.navigation.presentation.Email
import ui.navigation.presentation.Login
import ui.navigation.presentation.Phone

@Composable
public fun LoginScreen(
    modifier: Modifier = Modifier,
    route: Login = Login,
    config: AuthConfig = AuthConfig(),
    state: LoginState = LoginState(),
    onAction: (LoginAction) -> Unit = {},
    onNavigationActions: (Array<NavigationAction>) -> Unit = {},
): Unit = Box(
    modifier = modifier,
    contentAlignment = Alignment.Center,
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .fillMaxHeight()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Default.Login,
            contentDescription = stringResource(Res.string.login),
            modifier = Modifier.size(128.dp),
        )

        Text(
            text = stringResource(Res.string.login),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
            overflow = TextOverflow.Clip,
            maxLines = 1,
        )

        val focusRequesters = remember { List(3) { FocusRequester() } }

        LaunchedEffect(Unit) {
            focusRequesters.first().requestFocus()
        }

        val color = LocalContentColor.current.orErrorColor(state.error != null)

        TextField(
            value = state.username,
            onValueChange = { value -> onAction(LoginAction.SetUsername(value)) },
            modifier = Modifier.fillMaxWidth(0.8f).focusRequester(focusRequesters[0]),
            label = {
                Text(
                    text = stringResource(Res.string.username),
                    color = color,
                    overflow = TextOverflow.Clip,
                    maxLines = 1,
                )
            },
            placeholder = {
                Text(
                    text = stringResource(Res.string.username),
                    color = color,
                    overflow = TextOverflow.Clip,
                    maxLines = 1,
                )
            },
            leadingIcon = {
                Icon(
                    Icons.Default.Person,
                    stringResource(Res.string.username),
                    Modifier.padding(horizontal = 4.dp),
                    color,
                )
            },
            clearIcon = { action ->
                Icon(
                    Icons.Default.Close,
                    stringResource(Res.string.clear),
                    Modifier.padding(horizontal = 4.dp).clickable(onClick = action),
                    MaterialTheme.colorScheme.error,
                )
            },
            isError = state.error != null,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions { focusRequesters[1].requestFocus() },
            singleLine = true,
            outlined = true,
        )

        TextField(
            value = state.password,
            onValueChange = { value -> onAction(LoginAction.SetPassword(value)) },
            modifier = Modifier.fillMaxWidth(0.8f).focusRequester(focusRequesters[1]),
            label = {
                Text(
                    text = stringResource(Res.string.password),
                    color = color,
                    overflow = TextOverflow.Clip,
                    maxLines = 1,
                )
            },
            placeholder = {
                Text(
                    text = stringResource(Res.string.password),
                    color = color,
                    overflow = TextOverflow.Clip,
                    maxLines = 1,
                )
            },
            leadingIcon = {
                Icon(
                    Icons.Default.Lock,
                    stringResource(Res.string.password),
                    Modifier.padding(horizontal = 4.dp),
                    color,
                )
            },
            showIcon = { value, action ->
                Icon(
                    if (value) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    null,
                    Modifier.padding(horizontal = 4.dp).clickable(onClick = action),
                    color,
                )
            },
            clearIcon = { action ->
                Icon(
                    Icons.Default.Close,
                    stringResource(Res.string.clear),
                    Modifier.padding(horizontal = 4.dp).clickable(onClick = action),
                    MaterialTheme.colorScheme.error,
                )
            },
            isError = state.error != null,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions { focusRequesters[2].requestFocus() },
            singleLine = true,
            outlined = true,
            showValue = state.showPassword,
            onShowValueChange = { value -> onAction(LoginAction.SetShowPassword(value)) },
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = stringResource(Res.string.remember), overflow = TextOverflow.Clip, maxLines = 1)
            Spacer(Modifier.width(2.dp))
            Checkbox(
                checked = state.remember,
                onCheckedChange = { value -> onAction(LoginAction.SetRemember(value)) },
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(Res.string.reset_password),
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .clickable(enabled = config.password.phoneReset xor config.password.emailReset) {
                        if (state.username.isNotEmpty())
                            onNavigationActions(
                                arrayOf(
                                    NavigationAction.Push(
                                        if (config.password.phoneReset) Phone(state.username)
                                        else Email(state.username),
                                    ),
                                ),
                            )
                    },
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                overflow = TextOverflow.Clip,
                maxLines = 1,
            )

            if (config.password.phoneReset && config.password.emailReset) {
                IconButton(
                    onClick = {
                        if (state.username.isNotEmpty())
                            onNavigationActions(
                                arrayOf(
                                    NavigationAction.Push(Phone(state.username)),
                                ),
                            )
                    },
                ) {
                    Icon(Icons.Default.Phone, stringResource(Res.string.phone))
                }

                IconButton(
                    onClick = {
                        if (state.username.isNotEmpty())
                            onNavigationActions(
                                arrayOf(
                                    NavigationAction.Push(Email(state.username)),
                                ),
                            )
                    },
                ) {
                    Icon(Icons.Default.Email, stringResource(Res.string.email))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onAction(LoginAction.Login) },
            modifier = Modifier.fillMaxWidth(0.8f).focusRequester(focusRequesters[2]),
        ) {
            Text(text = stringResource(Res.string.login), overflow = TextOverflow.Clip, maxLines = 1)
        }

        LoginProviders(config, onAction)
    }
}

@Composable
private fun LoginProviders(
    config: AuthConfig,
    onAction: (LoginAction) -> Unit,
) = FlowRow(
    horizontalArrangement = Arrangement.Center,
    verticalArrangement = Arrangement.Center,
) {
    config.google?.let {
        IconButton(
            onClick = {
                onAction(LoginAction.LoginGoogle)
            },
        ) {
            PlainTooltipBox(tooltip = stringResource(Res.string.google)) {
                Icon(SimpleIcons.Google, stringResource(Res.string.google))
            }
        }
    }

    config.supabase?.let {
        IconButton(
            onClick = {
                onAction(LoginAction.LoginApple)
            },
        ) {
            PlainTooltipBox(tooltip = stringResource(Res.string.apple)) {
                Icon(SimpleIcons.Apple, stringResource(Res.string.apple))
            }
        }

        config.supabaseDefaultAuths.forEach { supabase ->
            IconButton(
                onClick = {
                    onAction(LoginAction.LoginSupabaseDefaultAuth(supabase.provider, supabase.config))
                },
            ) {
                PlainTooltipBox(tooltip = supabase.provider.name.asStringResource()) {
                    Icon(supabase.provider.imageVector(), supabase.provider.name.asStringResource())
                }
            }
        }

        config.supabaseOAuths.forEach { supabase ->
            IconButton(
                onClick = {
                    onAction(LoginAction.LoginSupabaseOAuth(supabase.provider, supabase.config))
                },
            ) {
                PlainTooltipBox(tooltip = supabase.provider.name.asStringResource()) {
                    Icon(supabase.provider.imageVector(), supabase.provider.name.asStringResource())
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewLoginScreen(): Unit = LoginScreen()
