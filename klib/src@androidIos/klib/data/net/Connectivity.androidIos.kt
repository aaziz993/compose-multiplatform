package klib.data.net

import dev.jordond.connectivity.Connectivity
import dev.jordond.connectivity.ConnectivityOptions
import kotlinx.coroutines.CoroutineScope

public actual fun createConnectivity(
    coroutineScope: CoroutineScope,
    options: ConnectivityOptions.Builder.() -> Unit,
): Connectivity = Connectivity(coroutineScope, options)
