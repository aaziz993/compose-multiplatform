package clib.presentation.components.auth

import androidx.compose.runtime.Composable
import klib.data.type.auth.AuthResource
import klib.data.type.auth.User

@Composable
public inline fun AuthComposable(
    auth: AuthResource?,
    provider: String?,
    user: User?,
    content: @Composable () -> Unit
) {
    if (auth?.validate(provider, user) != false) content()
}
