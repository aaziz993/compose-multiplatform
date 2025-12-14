@file:Suppress("ComposeCompositionLocalUsage")

package clib.presentation.auth

import androidx.compose.runtime.Composable
import clib.presentation.config.LocalConfig
import klib.data.auth.model.Auth
import klib.data.auth.model.AuthResource
import klib.data.auth.model.User

@Composable
public inline fun AuthComposable(
    id: String? = null,
    auth: Auth = LocalAuthState.current.value,
    authResource: AuthResource? = LocalConfig.current.auth.components[id],
    content: @Composable (User) -> Unit
) {
    if (authResource?.validate(auth.provider, auth.user) != false) content(auth.user!!)
}
