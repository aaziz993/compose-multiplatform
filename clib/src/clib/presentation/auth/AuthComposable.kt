@file:Suppress("ComposeCompositionLocalUsage")

package clib.presentation.auth

import androidx.compose.runtime.Composable
import klib.data.auth.model.Auth
import klib.data.auth.model.AuthResource
import klib.data.auth.model.User

@Composable
public inline fun AuthComposable(
    auth: Auth = LocalAuthState.current.auth,
    authResource: AuthResource? = AuthResource(),
    content: @Composable (User) -> Unit
) {
    if (authResource?.validate(auth.provider, auth.user) != false) content(auth.user!!)
}
