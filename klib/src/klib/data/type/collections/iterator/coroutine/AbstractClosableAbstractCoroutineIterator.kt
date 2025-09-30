package klib.data.type.collections.iterator.coroutine

public abstract class AbstractClosableAbstractCoroutineIterator<T> : AbstractCoroutineIterator<T>(), AutoCloseable {
    override fun close(): Unit = done()
}
