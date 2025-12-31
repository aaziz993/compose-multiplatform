package klib.data.network.http.client

import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsChannel
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
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

@Suppress("UNCHECKED_CAST")
public suspend fun <T> HttpResponse.bodyAsPolymorphic(
    format: StringFormat = Json.Default,
    nodeContent: () -> Any? = { }
): T = (if (status == HttpStatusCode.NoContent) nodeContent()
else format.decodeFromString(PolymorphicSerializer(Any::class), bodyAsText())) as T

@Suppress("UNCHECKED_CAST")
public fun <T> HttpResponse.bodyAsFlow(charset: Charset = Charsets.UTF_8, typeInfo: TypeInfo): Flow<T> =
    readChannelAsFlow(
        ::bodyAsChannel,
        call.client.converter(
            headers[HttpHeaders.ContentType]?.let(ContentType::parse)
                ?: error("Missing header ${HttpHeaders.ContentType}"),
        ),
        charset,
        typeInfo,
    )

public inline fun <reified T> HttpResponse.bodyAsFlow(charset: Charset = Charsets.UTF_8): Flow<T> =
    bodyAsFlow(charset, typeInfo<T>())

public fun HttpResponse.bodyAsAnyFlow(decoder: (String) -> Any? = Json.Default::decodeAnyFromString): Flow<Any?> =
    readChannelAsAnyFlow(::bodyAsChannel, decoder)


