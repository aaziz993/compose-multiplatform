package klib.data.net

import com.diamondedge.logging.logging
import dev.jordond.connectivity.Connectivity

private val log = logging()

public actual fun createConnectivity(): Connectivity = Connectivity {
    pollingIntervalMs = 5000
    onPollResult { response ->
        log.debug { "Poll Result: $response" }
    }
}
