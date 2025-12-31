@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package klib.data.network.http.server

import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.ContentConverter
import io.ktor.server.application.Application
import io.ktor.server.application.PipelineCall
import io.ktor.server.application.Plugin
import io.ktor.server.application.install
import io.ktor.util.AttributeKey
import io.ktor.util.pipeline.Pipeline

private val PLUGIN_CONFIGURATIONS: MutableMap<AttributeKey<*>, Any> = mutableMapOf()

public fun <P : Pipeline<*, PipelineCall>, B : Any, F : Any> P.installPlugin(
    plugin: Plugin<P, B, F>,
    configure: B.() -> Unit = {}
): F = install(plugin) {
    configure()
    PLUGIN_CONFIGURATIONS[plugin.key] = this
}

@Suppress("UnusedReceiverParameter", "UNCHECKED_CAST")
public fun <TConfiguration : Any> Application.pluginConfig(plugin: Plugin<*, TConfiguration, *>): TConfiguration? =
    PLUGIN_CONFIGURATIONS[plugin.key] as TConfiguration?

public fun Application.converters(contentType: ContentType): List<ContentConverter>? =
    pluginConfig(ContentNegotiation)
        ?.registrations
        ?.filter { registration -> registration.contentType == contentType }
        ?.map { registration -> registration.converter }

public fun Application.converter(contentType: ContentType): ContentConverter =
    converters(contentType)?.firstOrNull()
        ?: error("No suitable converter for $contentType")
