package clib.presentation.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import clib.presentation.noLocalProvidedFor
import klib.data.type.auth.model.Auth
import klib.data.type.auth.model.User

@Suppress("ComposeCompositionLocalUsage")
public val LocalAuthState: ProvidableCompositionLocal<AuthState> =
    staticCompositionLocalOf { noLocalProvidedFor("LocalAuthState") }

public class AuthState(initialValue: Auth = Auth()) {

    public var auth: Auth by mutableStateOf(initialValue)

    public fun setUser(user: User?) {
        auth = auth.copy(user = user)
    }

    public companion object Companion {

        public val Saver: Saver<AuthState, *> = listSaver(
            save = { listOf(it.auth) },
            restore = { AuthState(it[0]) },
        )
    }
}

@Composable
public fun rememberAuthState(initialValue: Auth = Auth()): AuthState =
    rememberSaveable(saver = AuthState.Saver) { AuthState(initialValue) }
