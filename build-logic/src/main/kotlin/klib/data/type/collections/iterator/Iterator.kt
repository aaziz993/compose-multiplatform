package klib.data.type.collections.iterator

import kotlin.NoSuchElementException

internal object EmptyIterator : Iterator<Nothing> {

    override fun hasNext(): Boolean = false

    override fun next(): Nothing = throw NoSuchElementException()
}

public fun emptyIterator(): Iterator<Nothing> = EmptyIterator

public fun <T : Any> Iterator<T>.nextOrNull(): T? =
    if (hasNext()) next() else null

public fun <T> Iterator<T>.next(
    count: Int,
    element: (Int, T) -> Unit,
): Int {
    var index = 0
    while (hasNext() && index < count) {
        element(index++, next())
    }
    return index
}

public fun <T> Iterator<T>.next(count: Int): List<T> =
    mutableListOf<T>().also { list ->
        next(count) { _, e ->
            list.add(e)
        }
    }

public fun <T> Iterator<T>.depthIterator(
    transform: IteratorDepthIterator<T>.(depth: Int, T) -> Iterator<T>?,
    removeLast: (depth: Int) -> Unit = {},
): Iterator<T> = IteratorDepthIterator(this, transform, removeLast)

public fun <T> Iterator<T>.depthIterator(
    initialTransform: T,
    transform: (transforms: List<T>, value: T) -> Iterator<T>?,
    removeLast: (transforms: List<T>, transform: T) -> Unit
): Iterator<T> {
    val transforms = mutableListOf(initialTransform)

    return depthIterator(
        { _, value -> transform(transforms, value)?.also { transforms.add(value) } },
    ) { removeLast(transforms, transforms.removeLast()) }
}

public class IteratorDepthIterator<T>(
    iterator: Iterator<T>,
    private val transform: IteratorDepthIterator<T>.(depth: Int, T) -> Iterator<T>?,
    private val removeLast: (depth: Int) -> Unit,
) : AbstractIterator<T>() {

    private val iterators = mutableListOf(iterator)

    override fun computeNext() {
        if (iterators.isEmpty()) {
            done()
            return
        }

        val last = iterators.last()

        if (last.hasNext()) {
            val next = last.next()

            setNext(next)

            if (next != null) {
                val transformed = transform(iterators.size, next)

                if (transformed != null) iterators.add(transformed)
            }

            return
        }

        removeLast(iterators.size)

        iterators.removeLast()
    }
}

public fun <T> Iterator<T>.breadthIterator(
    transform: IteratorBreadthIterator<T>.(Int, T) -> Iterator<T>?,
    removeFirst: () -> Unit = {},
): Iterator<T> = IteratorBreadthIterator(this, transform, removeFirst)

public class IteratorBreadthIterator<T>(
    iterator: Iterator<T>,
    private val transform: IteratorBreadthIterator<T>.(Int, T) -> Iterator<T>?,
    private val removeFirst: () -> Unit,
) : AbstractIterator<T>() {

    private val iterators = mutableListOf(iterator)
    private var isStop: Boolean = false

    public fun stop() {
        isStop = true
    }

    override fun computeNext() {
        do {
            val first = iterators.first()
            if (first.hasNext()) {
                val next = first.next()

                val transformed = transform(iterators.size - 1, next)

                if (isStop) {
                    break
                }

                if (transformed == null) {
                    setNext(next)
                    return
                }
                else {
                    iterators.add(transformed)
                }
            }
            else {
                iterators.removeFirst()
                removeFirst()
            }
        } while (iterators.isNotEmpty())

        done()
    }
}
