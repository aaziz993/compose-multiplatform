@file:Suppress("ComposeCompositionLocalUsage")

package clib.presentation.auth

import androidx.compose.runtime.Composable
import klib.data.type.auth.AuthResource
import klib.data.type.auth.model.Auth
import klib.data.type.auth.model.User

@Composable
public inline fun AuthComposable(
    auth: Auth = LocalAuthState.current.auth,
    authResource: AuthResource? = AuthResource(),
    content: @Composable (User) -> Unit
) {
    if (authResource?.validate(auth.provider, auth.user) != false) content(auth.user!!)
}
