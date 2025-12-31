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
import klib.auth.model.Auth
import klib.auth.model.User

@Suppress("ComposeCompositionLocalUsage")
public val LocalAuthState: ProvidableCompositionLocal<AuthState> = staticCompositionLocalOf(::AuthState)

public class AuthState(initialValue: Auth = Auth()) {

    public var value: Auth by mutableStateOf(initialValue)

    public fun setUser(user: User) {
        value = value.copy(user = user)
    }

    public fun reset() {
        value = Auth()
    }

    public companion object Companion {

        public val Saver: Saver<AuthState, *> = listSaver(
            save = { listOf(it.value) },
            restore = { AuthState(it[0]) },
        )
    }
}

@Composable
public fun rememberAuthState(initialValue: Auth = Auth()): AuthState =
    rememberSaveable(saver = AuthState.Saver) { AuthState(initialValue) }
