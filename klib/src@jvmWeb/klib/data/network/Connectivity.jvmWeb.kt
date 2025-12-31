package klib.data.network

import com.diamondedge.logging.logging
import dev.jordond.connectivity.Connectivity
import dev.jordond.connectivity.ConnectivityOptions
import dev.jordond.connectivity.HttpConnectivityOptions
import kotlinx.coroutines.CoroutineScope

private val log = logging("Connectivity")

public actual fun createConnectivity(
    coroutineScope: CoroutineScope,
    options: ConnectivityOptions.Builder.() -> Unit,
): Connectivity = Connectivity(
    HttpConnectivityOptions(
        pollingIntervalMs = 5000,
    ) { response ->
        log.debug("Poll Result") { response }
    },
    coroutineScope,
)
