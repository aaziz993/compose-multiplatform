package klib.data.filesystem

import klib.data.BUFFER_SIZE
import klib.data.type.collections.iterator.ByteArrayClosableIterator
import klib.data.type.collections.iterator.next
import kotlinx.io.Buffer
import kotlinx.io.RawSource
import kotlinx.io.readByteArray

// /////////////////////////////////////////////////////ITERATOR////////////////////////////////////////////////////////
public fun RawSource.iterator(bufferSize: Int = BUFFER_SIZE): ByteArrayClosableIterator =
    Buffer().let { buffer ->
        ByteArrayClosableIterator(
            { array ->
                readAtMostTo(buffer, array.size.toLong())
                    .also { size ->
                        buffer.readByteArray(size.toInt()).copyInto(array)
                    }.toInt()
            },
            ::close,
            bufferSize,
        )
    }

// ////////////////////////////////////////////////////////SOURCE///////////////////////////////////////////////////////
public fun Iterator<Byte>.asSource(): RawSource = IteratorSource(this)

private class IteratorSource(
    private val iterator: Iterator<Byte>,
) : RawSource {

    override fun readAtMostTo(sink: Buffer, byteCount: Long): Long = iterator.next(byteCount.toInt()).let {
        sink.write(it.toByteArray())
        it.size.toLong()
    }

    override fun close(): Unit = Unit
}
