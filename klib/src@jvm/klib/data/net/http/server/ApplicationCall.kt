package klib.data.net.http.server

import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respondOutputStream
import klib.data.type.collections.writeToOutputStream
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

public suspend fun ApplicationCall.respondFlow(
    contentType: ContentType? = null,
    status: HttpStatusCode? = null,
    flow: Flow<ByteArray>,
) = respondOutputStream(contentType, status, flow::writeToOutputStream)

public suspend fun ApplicationCall.respondFlow(
    contentType: ContentType? = null,
    status: HttpStatusCode? = null,
    flow: Flow<String>,
) = respondFlow(contentType, status, flow.map(String::encodeToByteArray))
