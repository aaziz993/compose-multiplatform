@file:Suppress("ComposeCompositionLocalUsage")

package clib.presentation.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import clib.presentation.noLocalProvidedFor
import klib.data.type.auth.AuthResource
import klib.data.type.auth.model.Auth

public val LocalAuth: ProvidableCompositionLocal<Auth> = staticCompositionLocalOf { noLocalProvidedFor("LocalAuth") }

@Composable
public inline fun AuthComposable(
    authResource: AuthResource?,
    content: @Composable () -> Unit
): Unit = LocalAuth.current.let { auth ->
    if (authResource?.validate(auth.provider, auth.user) != false) content()
}
