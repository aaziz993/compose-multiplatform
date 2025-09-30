package klib.data.type.collections.iterator

import klib.data.BUFFER_SIZE

public class ByteArrayClosableIterator(
    private val nextBlock: (ByteArray) -> Int,
    private val closeBlock: () -> Unit = {},
    bufferSize: Int = BUFFER_SIZE,
) : AbstractClosableAbstractIterator<ByteArray>() {

    private val byteArray = ByteArray(bufferSize)

    override fun computeNext() {
        val read = nextBlock(byteArray)
        if (read == -1) {
            close()
            closeBlock()
        }
        else setNext(if (read < byteArray.size) byteArray.copyOfRange(0, read) else byteArray)
    }
}
