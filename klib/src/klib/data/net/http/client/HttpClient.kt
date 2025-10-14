@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package klib.data.net.http.client

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.ContentConverter
import io.ktor.util.AttributeKey
import klib.data.net.http.client.model.Pin

private val PLUGIN_CONFIGURATIONS: MutableMap<AttributeKey<*>, Any> = mutableMapOf()

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





