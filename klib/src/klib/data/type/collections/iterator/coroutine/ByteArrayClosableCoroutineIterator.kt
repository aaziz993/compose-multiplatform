package klib.data.type.collections.iterator.coroutine

import klib.data.BUFFER_SIZE

public class ByteArrayClosableCoroutineIterator(
    private val nextBlock: suspend (ByteArray) -> Int,
    private val closeBlock: () -> Unit = {},
    bufferSize: Int = BUFFER_SIZE,
) : AbstractClosableAbstractCoroutineIterator<ByteArray>() {

    private val byteArray = ByteArray(bufferSize)

    override suspend fun computeNext() {
        val read = nextBlock(byteArray)
        if (read == -1) {
            close()
            closeBlock()
        }
        else setNext(if (read < byteArray.size) byteArray.copyOfRange(0, read) else byteArray)
    }
}
