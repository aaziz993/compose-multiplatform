@file:OptIn(InternalAPI::class)

package klib.data.net.http.client

import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.deserialize
import io.ktor.util.reflect.TypeInfo
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.InternalAPI
import io.ktor.utils.io.charsets.Charset
import io.ktor.utils.io.charsets.Charsets
import klib.data.BUFFER_SIZE
import klib.data.net.iterator
import klib.data.type.collections.iterator.coroutine.forEach
import klib.data.type.serialization.json.decodeAnyFromString
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

@Suppress("UNCHECKED_CAST")
public fun <T : Any> HttpResponse.bodyAsFlow(kClass: KClass<T>, bufferSize: Int = BUFFER_SIZE, charset: Charset = Charsets.UTF_8): Flow<T> = flow {
    val channel = bodyAsChannel()

    require(HttpHeaders.ContentType in headers) {
        "${HttpHeaders.ContentType} is not specified"
    }

    val contentType = headers[HttpHeaders.ContentType]!!.let(ContentType::parse)

    val suitableConverters = call.client.converters(contentType.withoutParameters())

    if (suitableConverters == null)
        channel.iterator(bufferSize).forEach { value ->
            emit(Json.Default.decodeFromString(kClass.serializer(), value.decodeToString()))
        }
    else channel.iterator(bufferSize).forEach { value ->
        emit(suitableConverters.deserialize(ByteReadChannel(value), TypeInfo(kClass), charset) as T)
    }
}

public inline fun <reified T : Any> HttpResponse.bodyAsFlow(bufferSize: Int = BUFFER_SIZE, charset: Charset = Charsets.UTF_8): Flow<T> =
    bodyAsFlow(T::class, bufferSize, charset)
