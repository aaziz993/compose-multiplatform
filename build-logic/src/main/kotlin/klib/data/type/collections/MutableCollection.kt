package klib.data.type.collections

import klib.data.type.functions.Equator
import klib.data.type.act
import klib.data.type.collections.list.asMutableList
import klib.data.type.primitives.toInt
import klib.data.type.collections.list.drop
import klib.data.type.collections.list.getOrPut
import klib.data.type.collections.list.put
import klib.data.type.collections.map.asMutableMap
import kotlin.collections.map
import kotlin.collections.plus
import kotlin.collections.set
import kotlin.collections.getOrPut

@Suppress("UNCHECKED_CAST")
public fun Any?.toNewMutableCollection(
    newMutableList: List<Any?>.(size: Int) -> Any = { size -> ArrayList<Any?>(size) },
    newMutableMap: Map<Any?, Any?>.(size: Int) -> Any = { size -> LinkedHashMap<Any?, Any?>(size) },
): Any = when (this) {
    null -> mutableMapOf<Any?, Any?>()

    is List<*> -> newMutableList(size)

    is Map<*, *> -> (this as Map<Any?, Any?>).newMutableMap(size)

    else -> throw IllegalArgumentException("Expected List or Map, but got ${this::class.simpleName}")
}

public infix fun <E> MutableCollection<E>.tryAddAll(elements: Iterable<E>?): Boolean? =
    elements?.let(::addAll)

public infix fun <E> MutableCollection<E>.updateAll(elements: Iterable<E>): Boolean? =
    addAll(elements.act(::clear))

public infix fun <E> MutableCollection<E>.tryUpdateAll(elements: Iterable<E>?): Boolean? =
    elements?.let(::updateAll)

public inline fun <E> MutableCollection<E>.removeFirstOrNull(predicate: (E) -> Boolean): E? {
    val iterator = iterator()

    while (iterator.hasNext()) {
        val next = iterator.next()
        if (predicate(next)) {
            iterator.remove()
            return next
        }
    }

    return null
}

public inline fun <E> MutableCollection<E>.removeFirst(predicate: (E) -> Boolean): E {
    val iterator = iterator()

    while (iterator.hasNext()) {
        val next = iterator.next()
        if (predicate(next)) {
            iterator.remove()
            return next
        }
    }

    throw NoSuchElementException()
}

public fun <E> MutableCollection<E>.updateSymmetric(other: Iterable<E>): Pair<List<E>, List<E>> =
    symmetricMinus(other).also { (left, right) ->
        removeAll(left)
        addAll(right)
    }

public fun <E> MutableCollection<E>.updateSymmetric(
    other: Iterable<E>,
    equator: Equator<E>,
): Pair<Iterable<E>, Iterable<E>> =
    symmetricMinus(other, equator).also { (left, right) ->
        removeAll(left)
        addAll(right)
    }

@Suppress("UNCHECKED_CAST")
public inline fun <K, V> Any.getOrPut(key: K, defaultValue: () -> V, set: Boolean = true): V = when (this) {
    is MutableList<*> -> asMutableList<V>().getOrPut(key!!.toInt(), defaultValue, set)

    is MutableMap<*, *> -> asMutableMap<K, V>().getOrPut(key, defaultValue)

    else -> throw IllegalArgumentException("Expected a MutableList or MutableMap, but got ${this::class.simpleName}")
}

@Suppress("UNCHECKED_CAST")
public operator fun <K, V> Any.set(key: K, value: V): Unit = when (this) {
    is MutableList<*> -> asMutableList<V>()[key!!.toInt()] = value

    is MutableMap<*, *> -> asMutableMap<K, V>()[key] = value

    else -> throw IllegalArgumentException("Expected a MutableList or MutableMap, but got ${this::class.simpleName}")
}

@Suppress("UNCHECKED_CAST")
public fun <K, V> Any.put(key: K, value: V, set: Boolean = false): V? = when (this) {
    is MutableList<*> -> asMutableList<V>().put(key!!.toInt(), value, set)

    is MutableMap<*, *> -> asMutableMap<K, V>().put(key, value)

    else -> throw IllegalArgumentException("Expected a MutableList or MutableMap, but got ${this::class.simpleName}")
}

@Suppress("UNCHECKED_CAST")
public fun <V> Any.remove(key: Any?): V? =
    when (this) {
        is MutableList<*> -> key!!.toInt().let { index ->
            if (index in indices) asMutableList<V>().removeAt(index) else null
        }

        is MutableMap<*, *> -> asMutableMap<Any?, V>().remove(key)

        else -> throw IllegalArgumentException("Expected a MutableList or MutableMap, but got ${this::class.simpleName}")
    }

public fun Any.clear(): Unit =
    when (this) {
        is MutableList<*> -> clear()

        is MutableMap<*, *> -> clear()

        else -> throw IllegalArgumentException("Expected a MutableList or MutableMap, but got ${this::class.simpleName}")
    }

////////////////////////////////////////////////////////////DEEP////////////////////////////////////////////////////////
public fun <P : Any> Any.deepRemove(
    vararg paths: P,
    pathKey: List<Pair<Any, Any?>>.(sourcePath: P) -> Any?,
    subPaths: List<Pair<Any, Any?>>.(value: Any, sourcePath: P) -> List<P>,
    getter: List<Pair<Any, Any?>>.() -> Any? = { last().first.getOrNull(last().second) },
    remover: List<Pair<Any, Any?>>.() -> Any? = {
        last().first.remove(last().second)
    },
): List<Pair<List<Pair<Any, Any?>>, Any?>> = buildList {
    DeepRecursiveFunction<DeepRemoveArgs<P>, Unit> { (sourcePaths, sources, sourceRemoves) ->
        sourcePaths.groupBy { path -> sources.pathKey(path) }.map { (key, paths) ->
            val currentSources = sources.replaceLast { copy(second = key) }

            val value = currentSources.getter()

            val nextSourcePaths = if (value == null) emptyList() else paths.flatMap { path ->
                currentSources.subPaths(value, path)
            }

            if (nextSourcePaths.isEmpty())
                return@map sourceRemoves.add(currentSources to currentSources.remover())

            callRecursive(
                DeepRemoveArgs(
                    nextSourcePaths,
                    currentSources + (value!! to null),
                    sourceRemoves,
                )
            )
        }
    }(
        DeepRemoveArgs(
            paths.toList(),
            listOf(this@deepRemove to null),
            this
        )
    )
}

private data class DeepRemoveArgs<P : Any>(
    val sourcePaths: List<P>,
    val sources: List<Pair<Any, Any?>>,
    val sourceRemoves: MutableList<Pair<List<Pair<Any, Any?>>, Any?>>,
)

public fun Any.deepRemove(
    vararg paths: List<Any?>,
    getter: List<Pair<Any, Any?>>.() -> Any? = { last().first.getOrNull(last().second) },
    remover: List<Pair<Any, Any?>>.() -> Unit = { last().first.remove<Any?>(last().second) },
): List<Pair<List<Pair<Any, Any?>>, Any?>> = deepRemove(
    *paths,
    pathKey = { sourcePath -> sourcePath.first() },
    subPaths = { _, sourcePath ->
        sourcePath.drop().let { path -> if (path.isEmpty()) emptyList() else listOf(path) }
    },
    getter = getter,
    remover = remover
)