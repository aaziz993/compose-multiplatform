package clib.presentation.components.connectivity

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import dev.jordond.connectivity.Connectivity
import dev.jordond.connectivity.Connectivity.Status

@Composable
public fun Connectivity(
    connectivity: Connectivity,
    connected: suspend () -> Unit = {},
    disconnected: suspend () -> Unit = {},
) {
    LaunchedEffect(connectivity) {
        var lastStatus: Status? = null
        connectivity.statusUpdates.collect { status ->
            if (status != lastStatus) {
                lastStatus = status
                when (status) {
                    is Status.Connected -> connected()
                    is Status.Disconnected -> disconnected()
                }
            }
        }
    }

    // Start monitoring if not already
    LaunchedEffect(Unit) {
        if (!connectivity.isMonitoring) connectivity.start()
    }

    DisposableEffect(Unit) {
        onDispose(connectivity::stop)
    }
}
