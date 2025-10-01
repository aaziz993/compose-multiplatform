@file:OptIn(InternalAPI::class)

package klib.data.net.http.client

import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.util.reflect.TypeInfo
import io.ktor.util.reflect.typeInfo
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.InternalAPI
import io.ktor.utils.io.charsets.Charset
import io.ktor.utils.io.charsets.Charsets
import io.ktor.utils.io.readByteArray
import io.ktor.utils.io.readRemaining
import klib.data.type.primitives.toLong
import klib.data.type.serialization.json.decodeAnyFromString
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.io.readString
import kotlinx.serialization.json.Json

@Suppress("UNCHECKED_CAST")
public fun <T : Any> HttpResponse.bodyAsFlow(typeInfo: TypeInfo, charset: Charset = Charsets.UTF_8): Flow<T> = flow {
    val channel = bodyAsChannel()

    val contentType = headers[HttpHeaders.ContentType]?.let(ContentType::parse)
        ?: error("Missing header ${HttpHeaders.ContentType}")

    val converter = call.client.converters(contentType.withoutParameters())?.firstOrNull()
        ?: error("No suitable converter for $contentType")

    while (!channel.isClosedForRead) {
        val lengthBytes = channel.readByteArray(Long.SIZE_BYTES)
        val itemSource = channel.readRemaining(lengthBytes.toLong())

        emit(converter.deserialize(charset, typeInfo, ByteReadChannel(itemSource)) as T)
    }
}

public inline fun <reified T : Any> HttpResponse.bodyAsFlow(charset: Charset = Charsets.UTF_8): Flow<T> =
    bodyAsFlow(typeInfo<T>(), charset)

public fun HttpResponse.bodyAsFlow(): Flow<Any?> = flow {
    val channel = bodyAsChannel()


    while (!channel.isClosedForRead) {
        val lengthBytes = channel.readByteArray(Long.SIZE_BYTES)
        val itemSource = channel.readRemaining(lengthBytes.toLong())

        emit(Json.Default.decodeAnyFromString(itemSource.readString()))
    }
}
