@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
@file:OptIn(ExperimentalForeignApi::class)

package klib.data.filesystem

import kotlinx.cinterop.ByteVarOf
import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.get
import kotlinx.cinterop.readBytes
import kotlinx.cinterop.set
import kotlinx.io.Buffer
import kotlinx.io.IOException
import kotlinx.io.bytestring.ByteString
import kotlinx.io.bytestring.ByteString.Companion.EMPTY
import kotlinx.io.files.FileNotFoundException
import kotlinx.io.readString
import platform.posix.ENOENT
import platform.posix.strerror

/** Copy [count] bytes from the memory at this pointer into a [ByteString]. */

public fun COpaquePointer.readByteString(count: Int): ByteString {
    return if (count == 0) EMPTY else ByteString(readBytes(count))
}

internal fun Buffer.writeNullTerminated(bytes: CPointer<ByteVarOf<Byte>>): Buffer = apply {
    var pos = 0
    while (true) {
        val byte = bytes[pos++]
        if (byte.toInt() == 0) {
            break
        }
        else {
            writeByte(byte)
        }
    }
}

internal fun Buffer.write(
    source: CPointer<ByteVarOf<Byte>>,
    offset: Int = 0,
    byteCount: Int,
): Buffer = apply {
    for (i in offset until offset + byteCount) {
        writeByte(source[i])
    }
}

internal fun Buffer.read(
    sink: CPointer<ByteVarOf<Byte>>,
    offset: Int = 0,
    byteCount: Int,
): Buffer = apply {
    for (i in offset until offset + byteCount) {
        sink[i] = readByte()
    }
}

internal fun errnoToIOException(errno: Int): IOException {
    val message = strerror(errno)
    val messageString = if (message != null) {
        Buffer().writeNullTerminated(message).readString()
    }
    else {
        "errno: $errno"
    }
    return when (errno) {
        ENOENT -> FileNotFoundException(messageString)
        else -> IOException(messageString)
    }
}
