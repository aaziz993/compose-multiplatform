package klib.data.type.collections

import klib.data.type.functions.Equator
import klib.data.type.functions.Merger
import klib.data.type.collections.list.add
import klib.data.type.collections.list.updateFirst
import klib.data.type.collections.list.updateLast
import klib.data.type.collections.map.with
import klib.data.type.primitives.ifTrue

public infix fun <E> Iterable<E>.tryPlus(elements: Iterable<E>?): Iterable<E> =
    elements?.let(::plus) ?: this

public val <E> Iterable<E>.entries: List<Map.Entry<Int, E>>
    get() = mapIndexed { index, value -> index with value }

public fun <K, V> Iterable<Map.Entry<K, V>>.toMap(): Map<K, V> = associate { (key, value) -> key to value }

public inline fun <K, V : Any> Iterable<K>.associateWithNotNull(valueSelector: (K) -> V?): Map<K, V> {
    @Suppress("UNCHECKED_CAST")
    return associateWith { valueSelector(it) }.filterValues { it != null } as Map<K, V>
}

public inline fun <E> Iterable<E>.replaceAt(index: Int, replacement: E.() -> E): List<E> {
    if (index == -1) return toList()

    val newList = toMutableList()

    newList[index] = newList[index].replacement()

    return newList
}

public inline fun <E> Iterable<E>.replaceFirst(replacement: E.() -> E): List<E> =
    toMutableList().updateFirst(replacement)

public inline fun <E> Iterable<E>.replaceFirst(predicate: (E) -> Boolean, replacement: E.() -> E): List<E> =
    replaceAt(indexOfFirst(predicate), replacement)

public fun <E> Iterable<E>.replaceFirst(elements: Iterable<E>, equator: Equator<E>): List<E> =
    elements.fold(toList()) { acc, element ->
        replaceFirst({ equator.equate(it, element) }) { element }
    }

public inline fun <E> Iterable<E>.replaceFirstOf(element: E, replacement: E.() -> E): List<E> =
    replaceAt(indexOf(element), replacement)

public fun <E> Iterable<E>.replaceFirstOf(elements: Iterable<E>): List<E> =
    elements.fold(toList()) { acc, element ->
        replaceFirstOf(element) { element }
    }

public inline fun <E> Iterable<E>.replaceLast(replacement: E.() -> E): List<E> = toMutableList().updateLast(replacement)

public inline fun <E> Iterable<E>.replaceLast(predicate: (E) -> Boolean, replacement: E.() -> E): List<E> =
    replaceAt(indexOfLast(predicate), replacement)

public fun <E> Iterable<E>.replaceLast(elements: Iterable<E>, equator: Equator<E>): List<E> =
    elements.fold(toList()) { acc, element ->
        replaceLast({ equator.equate(it, element) }) { element }
    }

public inline fun <E> Iterable<E>.replaceLastOf(element: E, replacement: E.() -> E): List<E> =
    replaceAt(lastIndexOf(element), replacement)

public infix fun <E> Iterable<E>.replaceLastOf(elements: Iterable<E>): List<E> =
    elements.fold(toList()) { acc, element ->
        replaceLastOf(element) { element }
    }

public fun <E> Iterable<E>.containsAll(other: Iterable<E>, equator: Equator<E>): Boolean =
    other.any { otherItem -> all { thisItem -> equator.equate(thisItem, otherItem) } }

public infix fun <E> Iterable<E>.containsAny(other: Iterable<E>): Boolean =
    any(other::contains)

public fun <E> Iterable<E>.containsAny(other: Iterable<E>, equator: Equator<E>): Boolean =
    any { thisItem -> other.any { otherItem -> equator.equate(thisItem, otherItem) } }

public infix fun <E> Iterable<E>.containsNone(other: Iterable<E>): Boolean =
    none(other::contains)

public fun <E> Iterable<E>.containsNone(other: Iterable<E>, equator: Equator<E>): Boolean =
    none { thisItem -> other.any { otherItem -> equator.equate(thisItem, otherItem) } }

public fun <E> Iterable<E>.contains(other: Iterable<E>, all: Boolean?): Boolean =
    when (all) {
        null -> containsNone(other)
        false -> containsAny(other)
        true -> all(other::contains)
    }

public fun <E> Iterable<E>.contains(
    other: Iterable<E>,
    all: Boolean?,
    equator: Equator<E>
): Boolean =
    when (all) {
        null -> containsNone(other, equator)
        false -> containsAny(other, equator)
        true -> containsAll(other, equator)
    }

public inline fun <reified E> Iterable<*>.containsInstance(): Boolean = any { it is E }

public inline fun <E, R : Any> Iterable<E>.firstNotThrowOf(transform: (E) -> R?): R? {
    var throwable: Throwable? = null
    for (element in this) {
        try {
            return transform(element)
        } catch (t: Throwable) {
            throwable = t
        }
    }
    throw throwable!!
}

public infix fun <E> Iterable<E>.symmetricMinus(other: Iterable<E>): Pair<List<E>, List<E>> {
    val left = this - other
    val right = other - this
    return left to right
}

public fun <E> Iterable<E>.minus(
    other: Iterable<E>,
    equator: Equator<E>
): Iterable<E> = filter { thisItem -> other.none { otherItem -> equator.equate(thisItem, otherItem) } }

public fun <E> Iterable<E>.symmetricMinus(
    other: Iterable<E>,
    equator: Equator<E>
): Pair<Iterable<E>, Iterable<E>> {
    val left = filter { thisItem -> other.none { otherItem -> equator.equate(thisItem, otherItem) } }
    val right = other.filter { otherItem -> none { thisItem -> equator.equate(thisItem, otherItem) } }
    return left to right
}

public fun <E> Iterable<E>.merge(merger: Merger<E>): List<E> = buildList {
    this@merge.forEach { element ->
        add(element, merger)
    }
}

public infix fun <E : Comparable<E>> Iterable<E>.findTopKHeap(k: Int): List<E> {
    val pq = PriorityQueue<E>(count())

    forEach {
        if (pq.size < k)
            pq.add(it)
        else if (it > pq.peek()) {
            pq.poll()
            pq.add(it)
        }
    }

    val result = mutableListOf<E>()

    try {
        for (i in k downTo 1) {
            result.add(pq.poll())
        }
    } catch (_: NoSuchElementException) {

    }
    return result
}

public fun Iterable<Boolean>.isAllTrue(): Boolean = all { it }

public fun Iterable<Boolean>.ifAllTrue(action: () -> Unit): Boolean? = isAllTrue().ifTrue(action)
