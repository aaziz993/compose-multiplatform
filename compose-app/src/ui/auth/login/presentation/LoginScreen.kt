package ui.auth.login.presentation

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Token
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import clib.data.type.orErrorColor
import clib.data.type.primitives.string.stringResource
import clib.generated.resources.apple
import clib.presentation.components.textfield.AdvancedTextField
import clib.presentation.icons.Facebook
import clib.presentation.icons.Github
import clib.presentation.icons.Gitlab
import clib.presentation.icons.Google
import clib.presentation.icons.Twitter
import clib.presentation.navigation.NavigationAction
import com.sunildhiman90.kmauth.google.compose.GoogleSignInButton
import com.sunildhiman90.kmauth.supabase.model.SupabaseDefaultAuthProvider
import com.sunildhiman90.kmauth.supabase.model.SupabaseOAuthProvider
import compose_app.generated.resources.Res
import compose_app.generated.resources.login
import compose_app.generated.resources.password
import compose_app.generated.resources.remember
import compose_app.generated.resources.reset_password
import compose_app.generated.resources.username
import data.type.primitives.string.asStringResource
import klib.data.auth.model.Auth
import klib.data.auth.model.toUser
import klib.data.config.auth.AuthConfig
import org.jetbrains.compose.resources.painterResource
import ui.auth.login.presentation.viewmodel.LoginAction
import ui.auth.login.presentation.viewmodel.LoginState
import ui.navigation.presentation.Login
import ui.navigation.presentation.Phone

@Composable
public fun LoginScreen(
    modifier: Modifier = Modifier,
    route: Login = Login,
    config: AuthConfig = AuthConfig(),
    state: LoginState = LoginState(),
    onAction: (LoginAction) -> Unit = {},
    onAuthChange: (Auth) -> Unit = {},
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
        )

        val focusList = remember { List(3) { FocusRequester() } }

        LaunchedEffect(Unit) {
            focusList.first().requestFocus()
        }

        val leadingIconColor = LocalContentColor.current.orErrorColor(state.error != null)

        AdvancedTextField(
            modifier = Modifier.fillMaxWidth(0.8f).focusRequester(focusList[0]),
            value = state.username,
            onValueChange = { value -> onAction(LoginAction.SetUsername(value)) },
            label = { Text(stringResource(Res.string.username)) },
            placeholder = { Text(stringResource(Res.string.username)) },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = leadingIconColor)
            },
            isError = state.error != null,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions { focusList[1].requestFocus() },
            singleLine = true,
            outlined = true,
        )

        AdvancedTextField(
            modifier = Modifier.fillMaxWidth(0.8f).focusRequester(focusList[1]),
            value = state.password,
            onValueChange = { value -> onAction(LoginAction.SetPassword(value)) },
            label = { Text(stringResource(Res.string.password)) },
            placeholder = { Text(stringResource(Res.string.password)) },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = leadingIconColor)
            },
            isError = state.error != null,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions { focusList[2].requestFocus() },
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
            Text(stringResource(Res.string.remember))
            Spacer(Modifier.width(2.dp))
            Checkbox(
                checked = state.remember,
                onCheckedChange = { value -> onAction(LoginAction.SetRemember(value)) },
            )
        }

        Text(
            text = stringResource(Res.string.reset_password),
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable {
                    onNavigationActions(
                        arrayOf(
                            NavigationAction.Push(Phone),
                        ),
                    )
                }
                .padding(vertical = 8.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onAction(LoginAction.Login) },
            modifier = Modifier.fillMaxWidth(0.8f).focusRequester(focusList[2]),
        ) {
            Text(text = stringResource(Res.string.login))
        }

        LoginProviders(config, onAction, onAuthChange)
    }
}

@Composable
private fun LoginProviders(
    config: AuthConfig,
    onAction: (LoginAction) -> Unit,
    onAuthChange: (Auth) -> Unit,
) = FlowRow(
    verticalArrangement = Arrangement.Center,
) {
    config.google?.let {
        GoogleSignInButton(modifier = Modifier) { user, error ->
            user?.let { onAuthChange(Auth("google", it.toUser())) }
            error?.let { onAction(LoginAction.SetError(it)) }
        }

        IconButton(
            onClick = {
                onAction(LoginAction.LoginGoogle)
            },
        ) {
            Icon(Icons.Default.Google, "Google")
        }
    }

    config.supabase?.let {
        IconButton(
            onClick = {
                onAction(LoginAction.LoginApple)
            },
            modifier = Modifier
                .clip(CircleShape),
        ) {
            Image(
                painter = painterResource(clib.generated.resources.Res.drawable.apple),
                contentDescription = "Apple",
                contentScale = ContentScale.Fit,
            )
        }

        config.supabaseDefaultAuths.forEach { supabase ->
            IconButton(
                onClick = {
                    onAction(LoginAction.LoginSupabaseDefaultAuth(supabase.provider, supabase.config))
                },
            ) {
                Icon(supabase.provider.imageVector(), supabase.provider.name.asStringResource())
            }
        }

        config.supabaseOAuths.forEach { supabase ->
            IconButton(
                onClick = {
                    onAction(LoginAction.LoginSupabaseOAuth(supabase.provider, supabase.config))
                },
            ) {
                Icon(supabase.provider.imageVector(), supabase.provider.name.asStringResource())
            }
        }
    }
}

private fun SupabaseDefaultAuthProvider.imageVector() = when (this) {
    SupabaseDefaultAuthProvider.EMAIL -> Icons.Default.Email
    SupabaseDefaultAuthProvider.ID_TOKEN -> Icons.Default.Token
    SupabaseDefaultAuthProvider.PHONE -> Icons.Default.Phone
}

private fun SupabaseOAuthProvider.imageVector() = when (this) {
    SupabaseOAuthProvider.GITHUB -> Icons.Default.Github
    SupabaseOAuthProvider.GITLAB -> Icons.Default.Gitlab
//    SupabaseOAuthProvider.BITBUCKET -> Icons.Default.Bitbucket
    SupabaseOAuthProvider.TWITTER -> Icons.Default.Twitter
//    SupabaseOAuthProvider.DISCORD -> Icons.Default.Discord
//    SupabaseOAuthProvider.SLACK -> Icons.Default.Slack
//    SupabaseOAuthProvider.SPOTIFY -> Icons.Default.Spotify
//    SupabaseOAuthProvider.TWITCH -> Icons.Default.Twitch
//    SupabaseOAuthProvider.LINKEDIN -> Icons.Default.Linkedin
//    SupabaseOAuthProvider.KEYCLOAK -> Icons.Default.Keycloak
    SupabaseOAuthProvider.GOOGLE -> Icons.Default.Google
    SupabaseOAuthProvider.FACEBOOK -> Icons.Default.Facebook
//    SupabaseOAuthProvider.AZURE -> Icons.Default.Azure
//    SupabaseOAuthProvider.APPLE -> Icons.Default.Apple
    else -> error("")
}

@Preview
@Composable
private fun PreviewLoginScreen(): Unit = LoginScreen()
