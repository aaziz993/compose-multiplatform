@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package klib.data.net.http.client

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.serialization.ContentConverter
import io.ktor.util.AttributeKey
import klib.data.net.http.client.model.Pin
import kotlinx.serialization.json.Json

private val PLUGIN_CONFIGURATIONS: MutableMap<AttributeKey<*>, Any> = mutableMapOf()

public val HTTP_CLIENT_JSON: Json = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
    explicitNulls = false
}

public expect fun createHttpClient(
    pins: List<Pin> = emptyList(),
    block: HttpClientConfig<*>.() -> Unit = {}
): HttpClient

public fun <TBuilder : Any, TPlugin : Any> HttpClientConfig<*>.installConfigAware(
    plugin: HttpClientPlugin<TBuilder, TPlugin>,
    configure: TBuilder.() -> Unit = {}
): Unit = install(plugin) {
    configure()
    PLUGIN_CONFIGURATIONS[plugin.key] = this
}

@Suppress("UnusedReceiverParameter", "UNCHECKED_CAST")
public fun <TConfig : Any> HttpClient.pluginConfig(plugin: HttpClientPlugin<TConfig, *>): TConfig? =
    PLUGIN_CONFIGURATIONS[plugin.key] as TConfig?

public fun HttpClient.converters(contentType: ContentType): List<ContentConverter>? =
    pluginConfig(ContentNegotiation)
        ?.registrations
        ?.filter { registration -> registration.contentTypeMatcher.contains(contentType) }
        ?.map { registration -> registration.converter }

public fun HttpClient.converter(contentType: ContentType): ContentConverter =
    converters(contentType)?.firstOrNull()
        ?: error("No suitable converter for $contentType")

public suspend inline fun <reified T> HttpClient.safeRequest(
    block: HttpRequestBuilder.() -> Unit,
): Result<T> = try {
    val response = request { block() }
    Result.success(response.body())
}
catch (e: Throwable) {
    Result.failure(e)
}

public suspend inline fun HttpClient.safeHttpRequest(
    block: HttpRequestBuilder.() -> Unit,
): Result<HttpResponse> = try {
    val response = request { block() }
    Result.success(response)
}
catch (e: Throwable) {
    Result.failure(e)
}



