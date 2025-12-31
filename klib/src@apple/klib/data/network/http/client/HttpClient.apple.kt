package klib.data.network.http.client

import io.ktor.client.*
import io.ktor.client.engine.darwin.*
import io.ktor.client.engine.darwin.certificates.*
import klib.data.network.http.client.model.Pin

public actual fun createHttpClient(
    pins: List<Pin>,
    block: HttpClientConfig<*>.() -> Unit
): HttpClient = HttpClient(Darwin) {
    engine {
        val builder = pins.fold(CertificatePinner.Builder()) { acc, pin ->
            acc.add(pin.pattern, *pin.pins.toTypedArray())
        }
        handleChallenge(builder.build())

        configureRequest {
            setAllowsCellularAccess(true)
        }
    }
    block()
}
