package klib.data.http

import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsChannel
import io.ktor.utils.io.core.readBytes
import io.ktor.utils.io.readRemaining
import kotlinx.io.Buffer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.io.decodeSourceToSequence

public suspend inline fun <reified T> HttpResponse.bodyAsSequence(json: Json = Json.Default): Sequence<T> {
    val channel = bodyAsChannel()
    return Buffer().use { buffer ->
        while (!channel.isClosedForRead) {
            val packet = channel.readRemaining(8192L) // 8KB buffer size
            while (!packet.exhausted()) {
                val bytes = packet.readBytes()
                buffer.write(bytes)
            }
        }
        json.decodeSourceToSequence<T>(buffer)
    }
}
