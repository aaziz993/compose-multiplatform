package klib.data.type.collections

import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.InternalAPI
import io.ktor.utils.io.flushIfNeeded
import klib.data.type.primitives.toByteArray

@OptIn(InternalAPI::class)
public suspend fun ByteWriteChannel.writeByteArrayWithLength(array: ByteArray) {
    val lengthBytes = array.size.toLong().toByteArray(Long.SIZE_BYTES)
    writeBuffer.write(lengthBytes + array)
    flushIfNeeded()
}
