package klib.data.type.collections.list

import klib.data.type.functions.Equator
import klib.data.type.functions.Merger
import kotlin.collections.getOrNull

@Suppress("UNCHECKED_CAST")
public val Any.asNullableMutableListOrNull: MutableList<Any?>?
    get() = this as? MutableList<Any?>

public val Any.asNullableMutableList: MutableList<Any?>
    get() = asNullableMutableListOrNull!!

@Suppress("UNCHECKED_CAST")
public val Any.asMutableListOrNull: MutableList<Any>?
    get() = this as? MutableList<Any>

public val Any.asMutableList: MutableList<Any>
    get() = asMutableListOrNull!!

@Suppress("UNCHECKED_CAST")
public fun <E> Any.asMutableListOrNull(): MutableList<E>? = this as? MutableList<E>

public fun <E> Any.asMutableList(): MutableList<E> = asMutableListOrNull()!!

public inline fun <E> MutableList<E>.updateAt(index: Int, replacement: E.() -> E): Boolean =
    if (index in indices) {
        this[index] = this[index].replacement()
        true
    }
    else false

public inline fun <E> MutableList<E>.updateFirst(replacement: E.() -> E): MutableList<E> = apply {
    if (isNotEmpty())
        this[0] = first().replacement()
}

public inline fun <E> MutableList<E>.updateFirst(predicate: (E) -> Boolean, replacement: E.() -> E): Int =
    indexOfFirst(predicate).also { index ->
        updateAt(index, replacement)
    }

public fun <E> MutableList<E>.updateFirst(elements: Iterable<E>, equator: Equator<E>): List<Int> =
    elements.map { element ->
        updateFirst({ equator.equate(it, element) }) { element }
    }

public inline fun <E> MutableList<E>.updateFirstOf(element: E, replacement: E.() -> E = { element }): Int =
    indexOf(element).also { index ->
        updateAt(index, replacement)
    }

public infix fun <E> MutableList<E>.updateFirstOf(elements: Iterable<E>): List<Int> = elements.map { element ->
    updateFirstOf(element) { element }
}

public inline fun <E> MutableList<E>.updateLast(replacement: E.() -> E): MutableList<E> = apply {
    if (isNotEmpty())
        this[size - 1] = last().replacement()
}

public inline fun <E> MutableList<E>.updateLast(predicate: (E) -> Boolean, replacement: E.() -> E): Int =
    indexOfLast(predicate).also { index ->
        updateAt(index, replacement)
    }

public fun <E> MutableList<E>.updateLast(elements: Iterable<E>, equator: Equator<E>): List<Int> =
    elements.map { element ->
        updateLast({ equator.equate(it, element) }) { element }
    }

public inline fun <E> MutableList<E>.updateLastOf(element: E, replacement: E.() -> E = { element }): Int =
    lastIndexOf(element).also { index ->
        updateAt(index, replacement)
    }

public infix fun <E> MutableList<E>.updateLastOf(elements: Iterable<E>): List<Int> = elements.map { element ->
    updateLastOf(element) { element }
}

@Suppress("UNCHECKED_CAST")
public fun <E> MutableList<E>.add(element: E, equator: Equator<E>, merger: Merger<E>): Boolean =
    if (updateFirst({ o1 -> equator.equate(o1, element) }) {
            merger.merge(this, element)
        } == -1) add(element) else false

@Suppress("UNCHECKED_CAST")
public fun <E> MutableList<E>.add(element: E, merger: Merger<E>): Boolean =
    if (updateFirstOf(element) { merger.merge(this, element) } == -1) add(element) else false

public inline fun <E> MutableList<E>.getOrPut(index: Int, putter: MutableList<E>.(index: Int) -> E): E =
    getOrNull(index) ?: putter(index)

public inline fun <E> MutableList<E>.put(
    index: Int,
    element: E,
    overrider: MutableList<E>.(index: Int, element: E) -> Unit
): E {
    if (index in indices) overrider(index, element) else add(index.coerceIn(0, size), element)

    return element
}

public fun <E> MutableList<E>.swap(i: Int, j: Int) {
    val tmp = this[i]
    this[i] = this[j]
    this[j] = tmp
}
