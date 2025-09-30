package klib.data.type.collections.iterator.coroutine

public class ClosableCoroutineIterator<T>(
    private val hasBlock: suspend () -> Boolean,
    private val nextBlock: suspend () -> T,
    private val closeBlock: () -> Unit = {},
) : AbstractClosableAbstractCoroutineIterator<T>() {

    override suspend fun computeNext() {
        if (hasBlock()) return setNext(nextBlock())

        close()
        closeBlock()
    }
}
