package klib.data.network.http.server

import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.OutgoingContent
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respondOutputStream
import io.ktor.util.reflect.TypeInfo
import io.ktor.util.reflect.typeInfo
import io.ktor.utils.io.charsets.Charset
import io.ktor.utils.io.charsets.Charsets
import klib.data.type.collections.writeToOutputStream
import klib.data.type.serialization.json.encodeAnyToString
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

@JvmName("respondByteArrayFlow")
public suspend fun ApplicationCall.respondFlow(
    contentType: ContentType? = null,
    status: HttpStatusCode? = null,
    flow: Flow<ByteArray>,
): Unit = respondOutputStream(contentType, status, flow::writeToOutputStream)

@JvmName("respondStringFlow")
public suspend fun ApplicationCall.respondFlow(
    contentType: ContentType? = null,
    status: HttpStatusCode? = null,
    flow: Flow<String>,
): Unit = respondFlow(contentType, status, flow.map(String::encodeToByteArray))

public suspend fun <T> ApplicationCall.respondFlow(
    contentType: ContentType = ContentType.Application.Json,
    status: HttpStatusCode? = null,
    charset: Charset = Charsets.UTF_8,
    typeInfo: TypeInfo,
    flow: Flow<T>
) {
    val converter = application.converter(contentType)

    val byteFlow: Flow<ByteArray> = flow.map { value ->
        val outgoing = converter.serialize(contentType, charset, typeInfo, value)

        check(outgoing is OutgoingContent.ByteArrayContent) {
            "Converter must return ByteArrayContent for streaming"
        }

        outgoing.bytes()
    }

    respondFlow(contentType, status, byteFlow)
}

public suspend inline fun <reified T> ApplicationCall.respondFlow(
    contentType: ContentType = ContentType.Application.Json,
    status: HttpStatusCode? = null,
    charset: Charset = Charsets.UTF_8,
    flow: Flow<T>
): Unit = respondFlow(contentType, status, charset, typeInfo<T>(), flow)

public suspend fun ApplicationCall.respondAnyFlow(
    contentType: ContentType = ContentType.Application.Json,
    status: HttpStatusCode? = null,
    charset: Charset = Charsets.UTF_8,
    encoder: (Any?) -> String = Json.Default::encodeAnyToString,
    flow: Flow<Any?>
): Unit = respondFlow(contentType, status, charset, flow.map(encoder))

