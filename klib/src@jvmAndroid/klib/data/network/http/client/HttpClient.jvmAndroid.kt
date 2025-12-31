package klib.data.network.http.client

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import java.util.concurrent.TimeUnit
import klib.data.network.http.client.model.Pin
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient

public actual fun createHttpClient(
    pins: List<Pin>,
    block: HttpClientConfig<*>.() -> Unit
): HttpClient = HttpClient(OkHttp) {
    engine {
        val certificatePinner = pins.fold(CertificatePinner.Builder()) { acc, pin ->
            acc.add(pin.pattern, *pin.pins.toTypedArray())
        }.build()

        preconfigured = OkHttpClient.Builder()
            .certificatePinner(certificatePinner)
            .build()

        config {
            retryOnConnectionFailure(true)
            connectTimeout(0, TimeUnit.SECONDS)
        }
    }
    block()
}
