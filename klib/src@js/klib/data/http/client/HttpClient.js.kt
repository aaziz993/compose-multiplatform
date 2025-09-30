package klib.data.http.client

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.js.Js
import klib.data.net.http.client.model.Pin

public actual fun createHttpClient(
    pins: List<Pin>,
    block: HttpClientConfig<*>.() -> Unit
): HttpClient = HttpClient(Js, block)
