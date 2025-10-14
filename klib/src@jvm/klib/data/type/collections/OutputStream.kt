package klib.data.io

import java.io.OutputStream
import klib.data.type.collections.withLengthHeader

public fun OutputStream.writeWithLengthHeader(array: ByteArray): Unit =
    write(array.withLengthHeader())
