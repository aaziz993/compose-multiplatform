package klib.data.type.collections.set

import klib.data.type.collections.list.add
import klib.data.type.functions.Equator
import klib.data.type.functions.Merger
import kotlin.collections.LinkedHashSet

public class MutableMergeSet<E>(
    initialCapacity: Int = 0,
    override val equator: Equator<E> = Equator.default(),
    override val merger: Merger<E> = Merger.default(),
) : MergeSet<E>, MutableSet<E> {
    public constructor(
        elements: Collection<E>,
        equator: Equator<E> = Equator.default(),
        merger: Merger<E> = Merger.default()
    ) : this(equator = equator, merger = merger) {
        addAll(elements)
    }

    private val delegate: MutableList<E> = ArrayList(initialCapacity)

    override val size: Int
        get() = delegate.size

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
