@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
@file:OptIn(InternalAPI::class)

package klib.data.net.http.client

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.contentnegotiation.ContentNegotiationConfig.ConverterRegistration
import io.ktor.client.plugins.plugin
import io.ktor.http.ContentType
import io.ktor.serialization.ContentConverter
import io.ktor.utils.io.InternalAPI
import klib.data.net.http.client.model.Pin

public expect fun createHttpClient(
    pins: List<Pin> = emptyList(),
    block: HttpClientConfig<*>.() -> Unit = {}
): HttpClient

//public fun HttpClientConfig<*>.consulDiscovery(
//    address: String,
//    loadBalancer: LoadBalancer = LoadBalancer.ROUND_ROBIN,
//    serviceName: String
//) = install(ConsulDiscovery(address, loadBalancer, serviceName))

public fun HttpClient.converters(contentType: ContentType): List<ContentConverter>? =
    plugin(ContentNegotiation)
        .config
        .registrations
        .filter { it.contentTypeMatcher.contains(contentType) }
        .map(ConverterRegistration::converter)
