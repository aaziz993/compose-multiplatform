package klib.data.net.http.client

import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.InternalAPI
import io.ktor.utils.io.writeByteArray
import klib.data.type.collections.withLengthHeader

@OptIn(InternalAPI::class)
public suspend fun ByteWriteChannel.writeByteArrayWithLength(array: ByteArray) =
    writeByteArray(array.withLengthHeader())
