package klib.data.type.collections.iterator

public abstract class AbstractClosableAbstractIterator<T> :
    AbstractIterator<T>(),
    AutoCloseable {
    override fun close(): Unit = done()
}
