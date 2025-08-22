package klib.data.type.collections.set

import klib.data.type.collections.list.add
import klib.data.type.functions.Equator
import klib.data.type.functions.Merger

public class MutableMergeSet<E>(
    override val equator: Equator<E> = Equator.default(),
    override val merger: Merger<E> = Merger.default(),
) : MergeSet<E>, MutableSet<E> {

    private val delegate: MutableList<E> = mutableListOf()

    override val size: Int
        get() = delegate.size

    @Suppress("UNCHECKED_CAST")
    override fun add(element: E): Boolean = delegate.add(element, equator, merger)

    override fun addAll(elements: Collection<E>): Boolean = elements.fold(true) { acc, e -> add(e) }

    override fun clear(): Unit = delegate.clear()

    override fun iterator(): MutableIterator<E> = delegate.iterator()

    override fun remove(element: E): Boolean = delegate.remove(element)

    override fun removeAll(elements: Collection<E>): Boolean = delegate.removeAll(elements)

    override fun retainAll(elements: Collection<E>): Boolean = delegate.retainAll(elements)

    override fun contains(element: E): Boolean = delegate.contains(element)

    override fun containsAll(elements: Collection<E>): Boolean = delegate.containsAll(elements)

    override fun isEmpty(): Boolean = delegate.isEmpty()
}

public fun <T> mutableMergeSetOf(
    vararg elements: T,
    equator: Equator<T> = Equator.default(),
    merger: Merger<T> = Merger.default()
): MutableMergeSet<T> =
    MutableMergeSet(equator, merger).apply {
        addAll(elements)
    }

public fun <T> mergeSetOf(
    vararg elements: T,
    equator: Equator<T> = Equator.default(),
    merger: Merger<T> = Merger.default()
): MergeSet<T> = mutableMergeSetOf(*elements, equator = equator, merger = merger)

public fun <T> emptyMergeSet(): MergeSet<T> = mergeSetOf()
