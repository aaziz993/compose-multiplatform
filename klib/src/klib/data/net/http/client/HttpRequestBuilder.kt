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
import klib.data.type.serialization.json.encodeAnyToString
import kotlin.jvm.JvmName
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

@JvmName("setBodyByteArrayFlow")
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

@JvmName("setBodyStringFlow")
public fun HttpRequestBuilder.setBody(
    flow: Flow<String>,
    contentType: ContentType? = null,
    status: HttpStatusCode? = null,
    contentLength: Long? = null,
): ChannelWriterContent = setBody(
    flow.map(String::encodeToByteArray),
    contentType,
    status,
    contentLength,
)

public fun <T> HttpRequestBuilder.setBody(
    client: HttpClient,
    flow: Flow<T>,
    typeInfo: TypeInfo,
    contentType: ContentType = ContentType.Application.Json,
    status: HttpStatusCode? = null,
    contentLength: Long? = null,
    charset: Charset = Charsets.UTF_8
): ChannelWriterContent {
    val converter = client.converter(contentType)

    val byteFlow: Flow<ByteArray> = flow.map { value ->
        val outgoing = converter.serialize(contentType, charset, typeInfo, value)

        check(outgoing is OutgoingContent.ByteArrayContent) {
            "Converter must return ByteArrayContent for streaming"
        }

        outgoing.bytes()
    }

    return setBody(byteFlow, contentType, status, contentLength)
}

public inline fun <reified T> HttpRequestBuilder.setBody(
    client: HttpClient,
    flow: Flow<T>,
    contentType: ContentType = ContentType.Application.Json,
    status: HttpStatusCode? = null,
    contentLength: Long? = null,
    charset: Charset = Charsets.UTF_8
): ChannelWriterContent = setBody(client, flow, typeInfo<T>(), contentType, status, contentLength, charset)

public fun HttpRequestBuilder.setAnyBody(
    flow: Flow<Any?>,
    encoder: (Any?) -> String = Json.Default::encodeAnyToString,
    contentType: ContentType = ContentType.Application.Json,
    status: HttpStatusCode? = null,
    contentLength: Long? = null,
): ChannelWriterContent = setBody(flow.map(encoder), contentType, status, contentLength)


