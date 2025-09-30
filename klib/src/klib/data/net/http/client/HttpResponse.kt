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
import klib.data.type.primitives.toInt
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Suppress("UNCHECKED_CAST")
public fun <T : Any> HttpResponse.bodyAsFlow(typeInfo: TypeInfo, charset: Charset = Charsets.UTF_8): Flow<T> = flow {
    val channel = bodyAsChannel()

    val contentType = headers[HttpHeaders.ContentType]?.let(ContentType::parse)
        ?: error("Missing header ${HttpHeaders.ContentType}")

    val converter = call.client.converters(contentType.withoutParameters())?.firstOrNull()
        ?: error("No suitable converter for $contentType")

    while (!channel.isClosedForRead) {
        val lengthBytes = channel.readByteArray(Int.SIZE_BYTES)
        val itemBytes = channel.readByteArray(lengthBytes.toInt())

        emit(converter.deserialize(charset, typeInfo, ByteReadChannel(itemBytes)) as T)
    }
}

public inline fun <reified T : Any> HttpResponse.bodyAsFlow(charset: Charset = Charsets.UTF_8): Flow<T> =
    bodyAsFlow(typeInfo<T>(), charset)
