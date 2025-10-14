package klib.data.type.collections

import java.io.OutputStream
import kotlinx.coroutines.flow.Flow

public suspend inline fun Flow<ByteArray>.writeToOutputStream(outputStream: OutputStream): Unit =
    outputStream.use {
        collect { value ->
            outputStream.write(value)
            outputStream.flush()
        }
    }
