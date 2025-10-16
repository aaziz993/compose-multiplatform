package klib.data.type.collections

import io.ktor.serialization.ContentConverter
import io.ktor.util.reflect.TypeInfo
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.charsets.Charset
import io.ktor.utils.io.charsets.Charsets
import io.ktor.utils.io.readByteArray
import io.ktor.utils.io.readRemaining
import klib.data.type.primitives.toLong
import klib.data.type.serialization.json.decodeAnyFromString
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.io.Source
import kotlinx.io.readString
import kotlinx.serialization.json.Json

public suspend fun ByteReadChannel.readByteArrayWithLength(): Source {
    val lengthBytes = readByteArray(Long.SIZE_BYTES)
    return readRemaining(lengthBytes.toLong())
}

public fun <T> ByteReadChannel.asFlow(decoder: suspend (Source) -> T) = flow {
    while (!isClosedForRead)
        emit(decoder(readByteArrayWithLength()))
}

@Suppress("UNCHECKED_CAST")
public fun <T> readChannelAsFlow(
    channel: suspend () -> ByteReadChannel,
    converter: ContentConverter,
    charset: Charset = Charsets.UTF_8,
    typeInfo: TypeInfo
): Flow<T> = flow {
    val channel = channel()

    while (!channel.isClosedForRead)
        emit(
            converter.deserialize(
                charset,
                typeInfo,
                ByteReadChannel(channel.readByteArrayWithLength()),
            ) as T,
        )
}

@Suppress("UNCHECKED_CAST")
public fun readChannelAsAnyFlow(
    channel: suspend () -> ByteReadChannel,
    decoder: (String) -> Any? = Json.Default::decodeAnyFromString
): Flow<Any?> = flow {
    val channel = channel()

    while (!channel.isClosedForRead)
        emit(decoder(channel.readByteArrayWithLength().readString()))
}
