package klib.data.net.http.client

import io.ktor.client.*
import io.ktor.client.engine.js.*
import io.ktor.util.Platform
import klib.data.net.http.client.model.Pin

public actual fun createHttpClient(
    pins: List<Pin>,
    block: HttpClientConfig<*>.() -> Unit
): HttpClient = HttpClient(Js, block)


