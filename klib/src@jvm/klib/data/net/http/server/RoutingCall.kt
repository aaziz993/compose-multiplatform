package klib.data.net.http.server

import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receiveChannel
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingCall
import io.ktor.util.reflect.TypeInfo
import io.ktor.util.reflect.typeInfo
import io.ktor.utils.io.charsets.Charset
import io.ktor.utils.io.charsets.Charsets
import klib.data.type.collections.readChannelAsAnyFlow
import klib.data.type.collections.readChannelAsFlow
import klib.data.type.serialization.json.decodeAnyFromString
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.StringFormat
import kotlinx.serialization.json.Json

public suspend fun RoutingCall.respondPolymorphic(value: Any?, format: StringFormat = Json.Default) {
    if (value == null) respond(HttpStatusCode.NoContent)
    else respond(
        HttpStatusCode.OK,
        format.encodeToString(
            PolymorphicSerializer(Any::class),
            value,
        ),
    )
}

@Suppress("UNCHECKED_CAST")
public fun <T> RoutingCall.receiveFlow(charset: Charset = Charsets.UTF_8, typeInfo: TypeInfo): Flow<T> =
    readChannelAsFlow(
        ::receiveChannel,
        application.converter(
            request.headers[HttpHeaders.ContentType]?.let(ContentType::parse)
                ?: error("Missing header ${HttpHeaders.ContentType}"),
        ),
        charset,
        typeInfo,
    )

public inline fun <reified T> RoutingCall.receiveFlow(charset: Charset = Charsets.UTF_8): Flow<T> =
    receiveFlow(charset, typeInfo<T>())

public fun RoutingCall.receiveAnyFlow(decoder: (String) -> Any? = Json.Default::decodeAnyFromString): Flow<Any?> =
    readChannelAsAnyFlow(::receiveChannel, decoder)
