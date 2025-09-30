package klib.data.net.http.client

import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.http.ContentType
import io.ktor.http.ContentType.Companion.parse
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.ChannelWriterContent
import kotlinx.coroutines.flow.Flow

public fun HttpRequestBuilder.setBody(
    flow: Flow<String>,
    contentType: ContentType? = null,
    status: HttpStatusCode? = null,
    contentLength: Long? = null,
) = ChannelWriterContent(
    flow::toByteWriteChannel,
    contentType,
    status,
    contentLength,
)

public fun <T : Any> HttpRequestBuilder.setBody(
    flow: Flow<T>,
    contentType: ContentType? = null,
    status: HttpStatusCode? = null,
    contentLength: Long? = null
) {
    val contentType = headers[HttpHeaders.ContentType]?.let(ContentType::parse)
    val client: HttpClient

    val suitableConverter = client.converters(contentType!!.withoutParameters())
        ?.firstOrNull()

    suitableConverter.serialize()
}
