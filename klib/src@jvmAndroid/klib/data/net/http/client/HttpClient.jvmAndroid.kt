package klib.data.net.http.client

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import klib.data.net.http.client.model.Pin
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient

public actual fun createHttpClient(
    pins: List<Pin>,
    block: HttpClientConfig<*>.() -> Unit
): HttpClient = HttpClient(OkHttp) {
    engine {
        val certificatePiner = pins.fold(CertificatePinner.Builder()) { acc, v ->
            acc.add(v.pattern, *v.pins.toTypedArray())
        }.build()

        preconfigured = OkHttpClient.Builder()
            .certificatePinner(certificatePiner)
            .build()
    }
    block()
}
