package klib.data.type.collections

import java.io.OutputStream

public fun OutputStream.writeWithLengthHeader(array: ByteArray): Unit = write(array.withLengthHeader())
