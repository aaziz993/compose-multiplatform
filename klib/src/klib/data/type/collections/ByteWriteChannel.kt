package klib.data.type.collections

import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.InternalAPI
import io.ktor.utils.io.writeByteArray

@OptIn(InternalAPI::class)
public suspend fun ByteWriteChannel.writeByteArrayWithLength(array: ByteArray): Unit =
    writeByteArray(array.withLengthHeader())
