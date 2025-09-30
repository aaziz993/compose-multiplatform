package klib.data.net.http.client

import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.ChannelWriterContent
import io.ktor.http.content.OutgoingContent
import io.ktor.util.reflect.TypeInfo
import io.ktor.util.reflect.typeInfo
import io.ktor.utils.io.charsets.Charset
import io.ktor.utils.io.charsets.Charsets
import klib.data.type.collections.writeToChannel
import klib.data.type.primitives.toByteArray
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Suppress("UnusedReceiverParameter")
public fun HttpRequestBuilder.setBody(
    flow: Flow<ByteArray>,
    contentType: ContentType? = null,
    status: HttpStatusCode? = null,
    contentLength: Long? = null,
): ChannelWriterContent = ChannelWriterContent(
    flow::writeToChannel,
    contentType,
    status,
    contentLength,
)

public fun HttpRequestBuilder.setBody(
    flow: Flow<String>,
    contentType: ContentType = ContentType.Application.Json,
    status: HttpStatusCode? = null,
    contentLength: Long? = null,
): ChannelWriterContent = setBody(
    flow.map(String::encodeToByteArray),
    contentType,
    status,
    contentLength,
)

context(client: HttpClient)
public fun <T : Any> HttpRequestBuilder.setBody(
    flow: Flow<T>,
    typeInfo: TypeInfo,
    contentType: ContentType,
    charset: Charset = Charsets.UTF_8
): ChannelWriterContent {
    val converter = client.converters(contentType.withoutParameters())?.firstOrNull()
        ?: error("No suitable converter for $contentType")

    val byteFlow: Flow<ByteArray> = flow.map { value ->
        val outgoing = converter.serialize(contentType, charset, typeInfo, value)

        check(outgoing is OutgoingContent.ByteArrayContent) {
            "Converter must return ByteArrayContent for streaming"
        }

        val bytes = outgoing.bytes()
        val lengthBytes = bytes.size.toByteArray(Int.SIZE_BYTES)

        lengthBytes + bytes
    }

    return setBody(byteFlow, contentType = contentType)
}

context(client: HttpClient)
public inline fun <reified T : Any> HttpRequestBuilder.setBody(
    flow: Flow<T>,
    contentType: ContentType,
    charset: Charset = Charsets.UTF_8
): ChannelWriterContent = setBody(flow, typeInfo<T>(), contentType, charset)
