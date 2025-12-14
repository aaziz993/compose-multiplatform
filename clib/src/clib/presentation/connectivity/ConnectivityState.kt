package clib.presentation.connectivity

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import clib.presentation.connectivity.model.Connectivity
import clib.presentation.noLocalProvidedFor

@Suppress("ComposeCompositionLocalUsage")
public val LocalConnectivityState: ProvidableCompositionLocal<ConnectivityState> =
    staticCompositionLocalOf { noLocalProvidedFor("LocalConnectivityState") }

public class ConnectivityState(initialValue: Connectivity = Connectivity()) {

    public var value: Connectivity by mutableStateOf(initialValue)

    public companion object Companion {

        public val Saver: Saver<ConnectivityState, *> = listSaver(
            save = { listOf(it.value) },
            restore = { ConnectivityState(it[0]) },
        )
    }
}

@Composable
public fun rememberConnectivityState(initialValue: Connectivity = Connectivity()): ConnectivityState =
    rememberSaveable(saver = ConnectivityState.Saver) { ConnectivityState(initialValue) }
