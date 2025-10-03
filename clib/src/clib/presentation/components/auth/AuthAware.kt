package clib.presentation.components.auth

import androidx.compose.runtime.Composable
import klib.data.type.auth.AuthResource
import klib.data.type.auth.User

@Composable
public inline fun AuthAware(
    auth: AuthResource?,
    provider: String?,
    user: User?,
    elseContent: @Composable () -> Unit = {},
    content: @Composable () -> Unit
): Unit = if (auth == null) {
    content()
    elseContent()
}
else {
    if ((provider in auth.providers && user?.roles?.let(auth::validate) == true)) content()
    else elseContent()
}
