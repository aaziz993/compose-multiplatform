@file:OptIn(InternalAPI::class)

package klib.data.net.http.client

import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsChannel
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.util.reflect.TypeInfo
import io.ktor.util.reflect.typeInfo
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.InternalAPI
import io.ktor.utils.io.charsets.Charset
import io.ktor.utils.io.charsets.Charsets
import klib.data.type.collections.readByteArrayWithLength
import klib.data.type.serialization.json.decodeAnyFromString
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.io.readString
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.StringFormat
import kotlinx.serialization.json.Json

@Suppress("UNCHECKED_CAST")
public suspend fun <T> HttpResponse.decodeFromAny(format: StringFormat = Json.Default): T =
    takeIf { response -> response.status != HttpStatusCode.NoContent }?.let { response ->
        format.decodeFromString(PolymorphicSerializer(Any::class), response.bodyAsText())
    } as T

@Suppress("UNCHECKED_CAST")
public fun <T : Any> HttpResponse.bodyAsFlow(typeInfo: TypeInfo, charset: Charset = Charsets.UTF_8): Flow<T> = flow {
    val channel = bodyAsChannel()

    val contentType = headers[HttpHeaders.ContentType]?.let(ContentType::parse)
        ?: error("Missing header ${HttpHeaders.ContentType}")

    val converter = call.client.converter(contentType)

    while (!channel.isClosedForRead)
        emit(
            converter.deserialize(
                charset,
                typeInfo,
                ByteReadChannel(channel.readByteArrayWithLength()),
            ) as T,
        )
}

public inline fun <reified T : Any> HttpResponse.bodyAsFlow(charset: Charset = Charsets.UTF_8): Flow<T> =
    bodyAsFlow(typeInfo<T>(), charset)

public fun HttpResponse.bodyAsFlow(decoder: (String) -> Any? = Json.Default::decodeAnyFromString): Flow<Any?> = flow {
    val channel = bodyAsChannel()

    while (!channel.isClosedForRead)
        emit(decoder(channel.readByteArrayWithLength().readString()))
}
