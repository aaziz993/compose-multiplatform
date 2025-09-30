package klib.data.type.collections.iterator.coroutine

import arrow.atomic.AtomicBoolean
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.writeFully
import io.ktor.utils.io.writer
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ChannelIterator
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

public interface CoroutineIterator<out T> {

    public suspend operator fun hasNext(): Boolean

    public suspend operator fun next(): T
}

internal object EmptyCoroutineIterator : CoroutineIterator<Nothing> {

    override suspend fun hasNext(): Boolean = false

    override suspend fun next(): Nothing = throw NoSuchElementException()
}

public fun <T> emptyCoroutineIterator(): CoroutineIterator<T> = EmptyCoroutineIterator

public suspend fun <T> CoroutineIterator<T>.next(
    count: Int,
    element: (Int, T) -> Unit,
): Int {
    var index = 0
    while (hasNext() && index < count) {
        element(index++, next())
    }
    return index
}

public suspend fun <T> CoroutineIterator<T>.next(count: Int): List<T> =
    mutableListOf<T>().also { list ->
        next(count) { _, e ->
            list.add(e)
        }
    }

public operator fun <T> CoroutineIterator<T>.iterator(): CoroutineIterator<T> = this

public suspend inline fun <T> CoroutineIterator<T>.forEach(operation: suspend (T) -> Unit) {
    for (element in this) operation(element)
}

public fun <T> CoroutineIterator<T>.depthIterator(
    transform: suspend CoroutineIteratorDepthIterator<T>.(depth: Int, T) -> CoroutineIterator<T>?,
    removeLast: suspend (depth: Int) -> Unit = {},
): CoroutineIterator<T> = CoroutineIteratorDepthIterator(this, transform, removeLast)

public fun <T> CoroutineIterator<T>.depthIterator(
    initialTransform: T,
    transform: suspend (transforms: List<T>, value: T) -> CoroutineIterator<T>?,
    removeLast: suspend (transforms: List<T>, transform: T) -> Unit
): CoroutineIterator<T> {
    val transforms = mutableListOf(initialTransform)

    return depthIterator(
        { _, value -> transform(transforms, value)?.also { transforms.add(value) } },
    ) { removeLast(transforms, transforms.removeLast()) }
}

public class CoroutineIteratorDepthIterator<T>(
    iterator: CoroutineIterator<T>,
    private val transform: suspend CoroutineIteratorDepthIterator<T>.(depth: Int, T) -> CoroutineIterator<T>?,
    private val removeLast: suspend (depth: Int) -> Unit,
) : AbstractCoroutineIterator<T>() {

    private val iterators = mutableListOf(iterator)

    override suspend fun computeNext() {
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

public fun <T> CoroutineIterator<T>.breadthIterator(
    transform: suspend CoroutineIteratorBreadthIterator<T>.(Int, T) -> CoroutineIterator<T>?,
    removeFirst: suspend () -> Unit = {},
): CoroutineIterator<T> = CoroutineIteratorBreadthIterator(this, transform, removeFirst)

public class CoroutineIteratorBreadthIterator<T>(
    iterator: CoroutineIterator<T>,
    private val transform: suspend CoroutineIteratorBreadthIterator<T>.(Int, T) -> CoroutineIterator<T>?,
    private val removeFirst: suspend () -> Unit = {},
) : AbstractCoroutineIterator<T>() {

    private val iterators = mutableListOf(iterator)
    private var isStop: Boolean = false

    public fun stop() {
        isStop = true
    }

    override suspend fun computeNext() {
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

// ////////////////////////////////////////////ITERATOR/////////////////////////////////////////////
public fun <T> CoroutineIterator<T>.scopeIterator(
    coroutineScope: CoroutineScope = CoroutineScope(
        Dispatchers.Default,
    )
): Iterator<T> =
    CoroutineIteratorIterator(this, coroutineScope)

private class CoroutineIteratorIterator<T>(
    private val iterator: CoroutineIterator<T>,
    private val coroutineScope: CoroutineScope,
) : AbstractIterator<T>() {

    override fun computeNext() {
        coroutineScope.launch {
            if (iterator.hasNext()) {
                setNext(next())
            }
            else {
                done()
            }
        }
    }
}

// ////////////////////////////////////////CHANNELITERATOR//////////////////////////////////////////
public fun <T> ChannelIterator<T>.coroutineIterator(): CoroutineIterator<T> =
    ChannelIteratorCoroutineIterator(this)

private class ChannelIteratorCoroutineIterator<out T>(
    private val channelIterator: ChannelIterator<T>,
) : CoroutineIterator<T> {

    override suspend fun hasNext(): Boolean = channelIterator.hasNext()

    override suspend fun next(): T = channelIterator.next()
}

public fun <T> CoroutineIterator<T>.channelIterator(): ChannelIterator<T> =
    CoroutineIteratorChannelIterator(this)

private class CoroutineIteratorChannelIterator<out T>(
    private val iterator: CoroutineIterator<T>,
) : ChannelIterator<T> {

    private var next: T? = null
    private var nextAssigned: AtomicBoolean = AtomicBoolean(false)

    override suspend fun hasNext(): Boolean =
        iterator.hasNext().also { hasNext ->
            if (!nextAssigned.getAndSet(true) && hasNext) {
                next = iterator.next()
            }
        }

    @Suppress("UNCHECKED_CAST")
    override fun next(): T =
        next.also {
            nextAssigned.set(false)
        } as T
}

// /////////////////////////////////////////RECEIVECHANNEL//////////////////////////////////////////
public fun <T> CoroutineIterator<T>.asReceiveChannel(
    coroutineScope: CoroutineScope = CoroutineScope(
        Dispatchers.Default,
    )
): ReceiveChannel<T> = coroutineScope.produce {
    forEach(::send)
    close()
}

// /////////////////////////////////////////BYTEREADCHANNEL/////////////////////////////////////////
public fun CoroutineIterator<ByteArray>.asByteReadChannel(
    coroutineContext: CoroutineContext = Dispatchers.Default,
    parent: Job = Job(),
    autoFlush: Boolean = true,
): ByteReadChannel = CoroutineScope(coroutineContext)
    .writer(parent, autoFlush) {
        forEach(channel::writeFully)
        if (!autoFlush) {
            channel.flush()
        }
    }.channel

// ////////////////////////////////////////////FLOW/////////////////////////////////////////////////
public fun <T> CoroutineIterator<T>.asFlow(): Flow<T> = flow { forEach(::emit) }
