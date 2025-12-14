package clib.presentation.connectivity

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.jordond.connectivity.Connectivity
import dev.jordond.connectivity.Connectivity.Status

@Suppress("ComposeCompositionLocalUsage")
public val LocalConnectivityStatus: ProvidableCompositionLocal<Status> = staticCompositionLocalOf { Status.Disconnected }

@Composable
public fun rememberConnectivity(connectivity: Connectivity): Status {

    val connectivityStatus by connectivity.statusUpdates
        .collectAsStateWithLifecycle(Status.Disconnected)

    DisposableEffect(connectivity) {
        connectivity.start()
        onDispose(connectivity::stop)
    }

    return connectivityStatus
}
