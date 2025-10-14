package klib.data.type.collections

import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readByteArray
import io.ktor.utils.io.readRemaining
import klib.data.type.primitives.toLong
import kotlinx.io.Source

public suspend fun ByteReadChannel.readByteArrayWithLength(): Source {
    val lengthBytes = readByteArray(Long.SIZE_BYTES)
    return readRemaining(lengthBytes.toLong())
}
