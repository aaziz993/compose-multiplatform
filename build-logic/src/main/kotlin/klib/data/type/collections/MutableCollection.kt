package klib.data.type.collections

import klib.data.type.functions.Equator
import klib.data.type.act
import klib.data.type.asInt
import klib.data.type.collections.list.drop
import klib.data.type.collections.list.getOrPut
import klib.data.type.collections.list.put
import klib.data.type.collections.map.getOrPut
import klib.data.type.collections.map.put
import kotlin.collections.map
import kotlin.collections.plus

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
public inline fun <V> Any.getOrPut(key: Any?, defaultValue: Any.(key: Any?) -> V): V =
    when (this) {
        is MutableList<*> -> (this as MutableList<V>).getOrPut(key!!.asInt, defaultValue)

        is MutableMap<*, *> -> (this as MutableMap<Any?, V>).getOrPut(key, defaultValue)

        else -> throw IllegalArgumentException("Expected a List or Map, but got ${this::class.simpleName}")
    }

@Suppress("UNCHECKED_CAST")
public fun <V> Any.put(
    key: Any?,
    value: V,
    overrideList: MutableList<V>.(index: Int, value: V) -> Unit = { _, value -> add(value) },
    overrideMap: MutableMap<Any?, V>.(key: Any?, value: V) -> Unit = { key, value -> put(key, value) }
): V = when (this) {
    is MutableList<*> -> (this as MutableList<V>).put(key!!.asInt, value, overrideList)

    is MutableMap<*, *> -> (this as MutableMap<Any?, V>).put(key, value, overrideMap)

    else -> throw IllegalArgumentException("Expected a MutableList or MutableMap, but got ${this::class.simpleName}")
}

@Suppress("UNCHECKED_CAST")
public fun <V> Any.remove(key: Any?): V? =
    when (this) {
        is MutableList<*> -> (this as MutableList<V>).removeAt(key!!.asInt)

        is MutableMap<*, *> -> (this as MutableMap<Any?, V>).remove(key)

        else -> throw IllegalArgumentException("Expected a MutableList or MutableMap, but got ${this::class.simpleName}")
    }

////////////////////////////////////////////////////////////DEEP////////////////////////////////////////////////////////
public fun <P> Any.deepRemove(
    vararg paths: P,
    pathKey: List<Pair<Any, Any?>>.(sourcePath: P) -> Any?,
    subPaths: List<Pair<Any, Any?>>.(value: Any, sourcePath: P) -> List<P>,
    getter: List<Pair<Any, Any?>>.() -> Any? = { last().first.getOrNull(last().second) },
    remover: List<Pair<Any, Any?>>.() -> Unit = {
        last().first.remove<Any?>(last().second)
    },
): Unit = DeepRecursiveFunction<DeepRemoveArgs<P>, Unit> { (
                                                               sourcePaths,
                                                               sourcePathKey,
                                                               sourceSubPaths,
                                                               sources,
                                                               sourceGetter,
                                                               sourceRemover
                                                           ) ->
    sourcePaths.groupBy { path -> sources.sourcePathKey(path) }.map { (key, paths) ->
        val currentSources = sources.replaceLast { copy(second = key) }

        val value = currentSources.sourceGetter()

        val nextSourcePaths = if (value == null) emptyList() else paths.flatMap { path ->
            currentSources.sourceSubPaths(value, path)
        }

        if (nextSourcePaths.isEmpty()) return@map currentSources.sourceRemover()

        callRecursive(
            DeepRemoveArgs(
                nextSourcePaths,
                sourcePathKey,
                sourceSubPaths,
                currentSources + (value!! to null),
                sourceGetter,
                sourceRemover
            )
        )
    }
}(
    DeepRemoveArgs(
        paths.toList(),
        pathKey,
        subPaths,
        listOf(this to null),
        getter,
        remover
    )
)

private data class DeepRemoveArgs<P>(
    val sourcePaths: List<P>,
    val sourcePathKey: List<Pair<Any, Any?>>.(sourcePath: P) -> Any?,
    val sourceSubPaths: List<Pair<Any, Any?>>.(value: Any, sourcePath: P) -> List<P>,
    val sources: List<Pair<Any, Any?>>,
    val sourceGetter: List<Pair<Any, Any?>>.() -> Any?,
    val sourceRemover: List<Pair<Any, Any?>>.() -> Unit,
)

public fun Any.deepRemove(
    vararg paths: List<Any?>,
    getter: List<Pair<Any, Any?>>.() -> Any? = { last().first.getOrNull(last().second) },
    remover: List<Pair<Any, Any?>>.() -> Unit = { last().first.remove<Any?>(last().second) },
): Any = deepRemove(
    *paths,
    pathKey = { sourcePath -> sourcePath.first() },
    subPaths = { _, sourcePath ->
        sourcePath.drop().let { path -> if (path.isEmpty()) emptyList() else listOf(path) }
    },
    getter = getter,
    remover = remover
)