package klib.data.fs

import klib.data.BUFFER_SIZE
import klib.data.type.collections.iterator.ByteArrayClosableIterator
import kotlinx.io.Source
import kotlinx.io.readByteArray

// /////////////////////////////////////////////////////ITERATOR////////////////////////////////////////////////////////
public fun Source.iterator(bufferSize: Int = BUFFER_SIZE): ByteArrayClosableIterator =
    ByteArrayClosableIterator(
        { array ->
            readByteArray(array.size).also { bytes ->
                bytes.copyInto(array)
            }.size
        },
        ::close, bufferSize,
    )
