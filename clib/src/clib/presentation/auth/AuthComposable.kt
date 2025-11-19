@file:Suppress("ComposeCompositionLocalUsage")

package clib.presentation.auth

import androidx.compose.runtime.Composable
import klib.data.type.auth.AuthResource
import klib.data.type.auth.model.Auth

@Composable
public inline fun AuthComposable(
    authResource: AuthResource? = AuthResource(),
    auth: Auth = LocalAuthState.current.auth,
    content: @Composable () -> Unit
) {
    if (authResource?.validate(auth.provider, auth.user) != false) content()
}
