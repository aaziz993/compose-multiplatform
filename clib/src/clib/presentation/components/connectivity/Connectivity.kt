package clib.presentation.components.connectivity

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import clib.presentation.components.connectivity.model.ConnectivityLocalization
import clib.presentation.event.snackbar.GlobalSnackbarEventController
import clib.presentation.event.snackbar.model.SnackbarEvent
import dev.jordond.connectivity.Connectivity
import dev.jordond.connectivity.Connectivity.Status

@Composable
public fun Connectivity(
    connectivity: Connectivity,
    localization: ConnectivityLocalization = ConnectivityLocalization(),
) {
    LaunchedEffect(connectivity) {
        connectivity.statusUpdates.collect { status ->
            when (status) {
                is Status.Connected -> {
                    GlobalSnackbarEventController.sendEvent(
                        SnackbarEvent(localization.connected),
                    )
                }

                is Status.Disconnected -> {
                    GlobalSnackbarEventController.sendEvent(
                        SnackbarEvent(localization.disconnected),
                    )
                }
            }
        }
    }

    // Start monitoring if not already
    LaunchedEffect(Unit) {
        if (!connectivity.isMonitoring) connectivity.start()
    }

    DisposableEffect(Unit) {
        onDispose { connectivity.stop() }
    }
}
