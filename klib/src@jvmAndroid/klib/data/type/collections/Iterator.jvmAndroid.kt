package klib.data.type.collections

import java.io.InputStream
import java.io.OutputStream
import klib.data.type.collections.iterator.next
import klib.data.type.primitives.number.toIntLSB

// /////////////////////////////////////////////////////INPUTSTREAM//////////////////////////////////////////////////////
public fun Iterator<Byte>.asInputStream(): InputStream = IteratorInputStream(this)

private class IteratorInputStream(
    private val iterator: Iterator<Byte>,
) : InputStream() {

    override fun read(): Int =
        if (iterator.hasNext()) {
            iterator.next().toIntLSB()
        }
        else {
            -1
        }

    override fun read(
        b: ByteArray,
        off: Int,
        len: Int,
    ): Int = iterator.next(len) { i, e -> b[off + i] = e }
        .let {
            if (it == 0) {
                -1
            }
            else {
                it
            }
        }

    override fun available(): Int = if (iterator.hasNext()) 1 else 0
}

// /////////////////////////////////////////////////////OUTPUTSTREAM//////////////////////////////////////////////////////
public fun Iterator<ByteArray>.asOutputStream(
    outputStream: OutputStream,
    keepFlushing: Boolean = true,
) {
    forEach {
        outputStream.write(it)
        if (keepFlushing) {
            outputStream.flush()
        }
    }
    if (!keepFlushing) {
        outputStream.flush()
    }
    outputStream.close()
}
