package klib.data.type.collections

import klib.data.type.collections.list.add
import klib.data.type.collections.list.updateFirst
import klib.data.type.collections.list.updateLast
import klib.data.type.collections.map.with
import klib.data.type.functions.Equator
import klib.data.type.functions.Merger
import klib.data.type.primitives.ifTrue
import kotlin.collections.Iterable
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.all
import kotlin.collections.any
import kotlin.collections.associate
import kotlin.collections.associateWith
import kotlin.collections.buildList
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.contains
import kotlin.collections.eachCount
import kotlin.collections.filter
import kotlin.collections.filterIndexed
import kotlin.collections.filterValues
import kotlin.collections.fold
import kotlin.collections.forEach
import kotlin.collections.groupingBy
import kotlin.collections.indexOf
import kotlin.collections.indexOfFirst
import kotlin.collections.indexOfLast
import kotlin.collections.lastIndexOf
import kotlin.collections.map
import kotlin.collections.mapIndexed
import kotlin.collections.minus
import kotlin.collections.none
import kotlin.collections.plus
import kotlin.collections.toList
import kotlin.collections.toMutableList
import kotlin.collections.toSet

public val <E> Iterable<E>.entries: List<Map.Entry<Int, E>>
    get() = mapIndexed { index, value -> index with value }

public infix fun <E> Iterable<E>.tryPlus(elements: Iterable<E>?): Iterable<E> =
    elements?.let(::plus) ?: this

public infix fun <E> Iterable<E>.minusIndices(indices: Iterable<Int>): List<E> = filterIndexed { index, _ ->
    index !in indices
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
        acc.replaceFirst({ equator.equate(it, element) }) { element }
    }

public inline fun <E> Iterable<E>.replaceFirstOf(element: E, replacement: E.() -> E): List<E> =
    replaceAt(indexOf(element), replacement)

public fun <E> Iterable<E>.replaceFirstOf(elements: Iterable<E>): List<E> =
    elements.fold(toList()) { acc, element ->
        acc.replaceFirstOf(element) { element }
    }

public inline fun <E> Iterable<E>.replaceLast(replacement: E.() -> E): List<E> = toMutableList().updateLast(replacement)

public inline fun <E> Iterable<E>.replaceLast(predicate: (E) -> Boolean, replacement: E.() -> E): List<E> =
    replaceAt(indexOfLast(predicate), replacement)

public fun <E> Iterable<E>.replaceLast(elements: Iterable<E>, equator: Equator<E>): List<E> =
    elements.fold(toList()) { acc, element ->
        acc.replaceLast({ equator.equate(it, element) }) { element }
    }

public inline fun <E> Iterable<E>.replaceLastOf(element: E, replacement: E.() -> E): List<E> =
    replaceAt(lastIndexOf(element), replacement)

public infix fun <E> Iterable<E>.replaceLastOf(elements: Iterable<E>): List<E> =
    elements.fold(toList()) { acc, element ->
        acc.replaceLastOf(element) { element }
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
        true -> all(other::contains)
        false -> containsAny(other)
    }

public fun <E> Iterable<E>.contains(
    other: Iterable<E>,
    all: Boolean?,
    equator: Equator<E>
): Boolean =
    when (all) {
        null -> containsNone(other, equator)
        true -> containsAll(other, equator)
        false -> containsAny(other, equator)
    }

public fun <E> Iterable<E>.equals(other: Iterable<E>, equator: Equator<E>): Boolean {
    val iter1 = this.iterator()
    val iter2 = other.iterator()

    while (iter1.hasNext() && iter2.hasNext()) {
        val e1 = iter1.next()
        val e2 = iter2.next()
        if (!equator.equate(e1, e2)) return false
    }

    return !iter1.hasNext() && !iter2.hasNext()
}

public inline fun <reified E> Iterable<*>.anyInstance(): Boolean = any { it is E }

public inline fun <E, R : Any> Iterable<E>.firstNotThrowOf(transform: (E) -> R?): R? {
    var throwable: Throwable? = null
    for (element in this) {
        try {
            return transform(element)
        }
        catch (t: Throwable) {
            throwable = t
        }
    }
    throw throwable!!
}

public infix fun <E> Iterable<E>.symmetricMinus(other: Iterable<E>): Pair<Set<E>, Set<E>> {
    val left = this.toSet() - other.toSet()
    val right = other.toSet() - this.toSet()
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

public fun <E> Iterable<E>.merge(equator: Equator<E>, merger: Merger<E>): List<E> = buildList {
    this@merge.forEach { element ->
        add(element, equator, merger)
    }
}

public inline fun <E, T> Iterable<E>.topKHeap(
    k: Int,
    crossinline selector: (E) -> T,
    comparator: Comparator<in T>
): PriorityQueue<T> = buildPriorityQueue(k + 1, comparator) {
    this@topKHeap.map(selector).forEach { element ->
        if (size < k) add(element)
        else if (comparator.compare(element, peek()) > 0) {
            poll()
            add(element)
        }
    }
}

public infix fun <E : Comparable<E>> Iterable<E>.topKElements(k: Int): PriorityQueue<E> =
    topKHeap(k, { element -> element }) { a, b -> a.compareTo(b) }

public infix fun <E : Comparable<E>> Iterable<E>.topKFrequent(k: Int): PriorityQueue<E> =
    groupingBy { element -> element }.eachCount().let { frequencyMap ->
        frequencyMap.entries.topKHeap(
            k,
            { (element, _) -> element },
            compareBy { element -> frequencyMap[element] },
        )
    }

public infix fun <E : Comparable<E>> Iterable<E>.findKthLargest(k: Int): E = topKElements(k).poll()

public fun Iterable<Boolean>.all(): Boolean = all { it }

public fun Iterable<Boolean>.ifAll(action: () -> Unit): Boolean? = all().ifTrue(action)

@Suppress("UNCHECKED_CAST")
public inline fun <K, V : Any> Iterable<K>.associateWithNotNull(valueSelector: (K) -> V?): Map<K, V> =
    associateWith(valueSelector).filterValues { it != null } as Map<K, V>

public fun <K, V> Iterable<Map.Entry<K, V>>.toMap(): Map<K, V> = associate { (key, value) -> key to value }
