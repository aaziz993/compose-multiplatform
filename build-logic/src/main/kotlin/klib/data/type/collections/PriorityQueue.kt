@file:Suppress("SameParameterValue")

package klib.data.type.collections

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.math.max

@Suppress("UNCHECKED_CAST")
public class PriorityQueue<T>(initialCapacity: Int = 16, private val comparator: Comparator<in T>? = null) :
    Collection<T> {

    init {
        require(initialCapacity >= 0) { "initialCapacity must be >= 0" }
    }

    private var arr: Array<T?> = arrayOfNulls(max(2, initialCapacity + 1))

    override var size: Int = 0
        private set

    public constructor(
        elements: Collection<T>,
        comparator: Comparator<in T>? = null
    ) : this(elements.size, comparator) {
        // bulk copy into 1-based backing array
        var i = 0
        for (e in elements) arr[++i] = e
        size = i
        // bottom-up heapify (linear time)
        for (j in size / 2 downTo 1) sink(j)
    }

    public fun add(element: T) {
        ensureCapacity(size + 1)
        arr[++size] = element
        swim(size)
    }

    public fun addAll(elements: Iterable<T>) {
        if (elements is Collection<*>) ensureCapacity(size + elements.size)
        for (e in elements) add(e)
    }

    public fun peek(): T {
        if (isEmpty()) throw NoSuchElementException()
        return arr[1] as T
    }

    public fun peekOrNull(): T? = if (isEmpty()) null else arr[1] as T

    public fun poll(): T {
        if (isEmpty()) throw NoSuchElementException()
        val res = arr[1] as T
        arr.swap(1, size)
        arr[size] = null
        size--
        if (isNotEmpty()) sink(1)
        resizeIfSparse()
        return res
    }

    public fun pollOrNull(): T? = if (isEmpty()) null else poll()

    public fun clear() {
        arr.fill(null, 1, size + 1)
        size = 0
    }

    public fun toList(): List<T> = buildList(size) {
        for (i in 1..size) add(arr[i] as T)
    }

    public fun toSortedList(): List<T> = toList().sortedWith(::compare)

    override fun isEmpty(): Boolean = size == 0

    override fun contains(element: T): Boolean {
        for (i in 1..size) if (arr[i] == element) return true
        return false
    }

    override fun containsAll(elements: Collection<T>): Boolean {
        for (element in elements) if (!contains(element)) return false
        return true
    }

    override fun iterator(): Iterator<T> = object : Iterator<T> {
        private var i = 1
        override fun hasNext(): Boolean = i <= size

        override fun next(): T {
            if (!hasNext()) throw NoSuchElementException()
            return arr[i++] as T
        }
    }

    private fun ensureCapacity(required: Int) {
        if (required >= arr.size) arr = arr.copyOf(max(arr.size * 2, required + 1))
    }

    private fun resizeIfSparse() {
        if (arr.size > 2 && size <= (arr.size - 1) / 4) arr = arr.copyOf(max(2, (arr.size / 2)))
    }

    private fun swim(pos: Int) {
        var i = pos
        while (i > 1 && greater(i / 2, i)) {
            arr.swap(i, i / 2)
            i /= 2
        }
    }

    private fun sink(a: Int) {
        var i = a
        while (2 * i <= size) {
            var j = 2 * i
            if (j < size && greater(j, j + 1)) j++
            if (!greater(i, j)) break
            arr.swap(i, j)
            i = j
        }
    }

    private fun greater(i: Int, j: Int): Boolean = compare(arr[i] as T, arr[j] as T) > 0

    private fun compare(a: T, b: T): Int = comparator?.compare(a, b) ?: (a as Comparable<T>).compareTo(b)
}

public fun <T> priorityQueueOf(elements: Collection<T>, comparator: Comparator<in T>? = null): PriorityQueue<T> =
    PriorityQueue(elements, comparator)

public inline fun <E> buildPriorityQueue(
    initialCapacity: Int = 16,
    comparator: Comparator<in E>? = null,
    builderAction: PriorityQueue<E>.() -> Unit
): PriorityQueue<E> {
    contract { callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE) }
    return PriorityQueue(initialCapacity, comparator).apply(builderAction)
}
