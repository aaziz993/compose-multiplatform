package klib.data.type.collections

import klib.data.type.asInt
import klib.data.type.collections.list.drop
import klib.data.type.collections.list.minusIndices
import klib.data.type.collections.map.asMap
import kotlin.collections.Collection
import kotlin.collections.Iterator
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.all
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.contains
import kotlin.collections.emptyList
import kotlin.collections.first
import kotlin.collections.forEach
import kotlin.collections.getOrElse
import kotlin.collections.groupBy
import kotlin.collections.indices
import kotlin.collections.isNotEmpty
import kotlin.collections.last
import kotlin.collections.listOf
import kotlin.collections.map
import kotlin.collections.minus
import kotlin.collections.mutableMapOf
import kotlin.collections.partition
import kotlin.collections.plus
import kotlin.collections.toList
import kotlin.collections.withIndex
import kotlin.collections.zipWithNext

public fun <T : Collection<E>, E> T.takeIfNotEmpty(): T? = takeIf(Collection<*>::isNotEmpty)

public val Any.entriesOrNull: Collection<Map.Entry<Any?, Any?>>?
    get() = when (this) {
        is List<*> -> entries

        is Map<*, *> -> entries

        else -> null
    }

public val Any.entries: Collection<Map.Entry<Any?, Any?>>
    get() = entriesOrNull!!

public fun Any.iteratorOrNull(): Iterator<Map.Entry<Any?, Any?>>? = entriesOrNull?.iterator()

public fun Any.iterator(): Iterator<Map.Entry<Any?, Any?>> = iteratorOrNull()!!

public fun Collection<Int>.isZeroConsecutive(): Boolean = withIndex().all { (index, element) -> index == element }

public fun Collection<Int>.isConsecutive(): Boolean = zipWithNext().all { (a, b) -> b == a + 1 }

public fun <T : Any, K> T.getOrElse(key: K, defaultValue: T.(K) -> Any?): Any? =
    when (this) {
        is List<*> -> getOrElse(key!!.asInt) { defaultValue(key) }

        is Map<*, *> -> asMap<K, Any?>().getOrElse(key) { defaultValue(key) }

        else -> throw IllegalArgumentException("Expected a List or Map, but got ${this::class.simpleName}")
    }

public fun Any.getOrNull(key: Any?): Any? = getOrElse(key) { null }

public operator fun Any.get(key: Any?): Any? =
    getOrElse(key) { key -> throw IllegalArgumentException("Unknown key '$key' in: ${this::class::simpleName}") }

@Suppress("UNCHECKED_CAST")
public fun Any.containsKey(key: Any?): Boolean =
    when (this) {
        is List<*> -> key!!.asInt in indices

        is Map<*, *> -> (this as Map<Any?, *>).containsKey(key)

        else -> throw IllegalArgumentException("Expected a List or Map, but got ${this::class.simpleName}")
    }

@Suppress("UNCHECKED_CAST")
public fun <T : Any> T.minusKeys(vararg keys: Any?): T = when (this) {
    is List<*> -> minusIndices(keys.map { key -> key!!.asInt })

    is Map<*, *> -> minus(keys)

    else -> throw IllegalArgumentException("Expected a List or Map, but got ${this::class.simpleName}")
} as T

//////////////////////////////////////////////////////////SHALLOW//////////////////////////////////////////////////////
@Suppress("UNCHECKED_CAST")
public fun <T : Any> Any.map(
    sourceIterator: Any.() -> Iterator<Map.Entry<Any?, Any?>> = Any::iterator,
    sourceTransform: Any.(key: Any?, value: Any?) -> Pair<Any?, Any?>? = { key, value -> key to value },
    destination: T = toNewMutableCollection() as T,
    destinationSetter: T.(key: Any?, value: Any?) -> Unit = { key, value -> put(key, value) },
): T {
    sourceIterator().forEach { (key, value) ->
        val (key, value) = sourceTransform(key, value) ?: return@forEach

        destination.destinationSetter(key, value)
    }

    return destination
}

@Suppress("UNCHECKED_CAST")
public fun <T : Any> Any.mapKeys(
    sourceIterator: Any.() -> Iterator<Map.Entry<Any?, Any?>> = Any::iterator,
    sourceFilter: Any.(key: Any?, value: Any?) -> Boolean = { _, _ -> true },
    sourceTransform: Any.(key: Any?, value: Any?) -> Any?,
    destination: T = toNewMutableCollection() as T,
    destinationSetter: T.(key: Any?, value: Any?) -> Unit = { key, value -> put(key, value) },
): T = map(
    sourceIterator,
    { key, value ->
        if (sourceFilter(key, value)) sourceTransform(key, value) to value else null
    },
    destination,
    destinationSetter,
)

@Suppress("UNCHECKED_CAST")
public fun <T : Any> Any.mapValues(
    sourceIterator: Any.() -> Iterator<Map.Entry<Any?, Any?>> = Any::iterator,
    sourceFilter: Any.(key: Any?, value: Any?) -> Boolean = { _, _ -> true },
    sourceTransform: Any.(key: Any?, value: Any?) -> Any?,
    destination: T = toNewMutableCollection() as T,
    destinationSetter: T.(key: Any?, value: Any?) -> Unit = { key, value -> put(key, value) },
): T = map(
    sourceIterator,
    { key, value ->
        if (sourceFilter(key, value)) key to sourceTransform(key, value) else null
    },
    destination,
    destinationSetter,
)

@Suppress("UNCHECKED_CAST")
public fun <T : Any, K> Any.slice(
    vararg sourceKeys: K,
    sourceGetter: Any.(key: K) -> Any? = Any::getOrNull,
    destination: T = toNewMutableCollection() as T,
    destinationSetter: T.(key: K, value: Any?) -> Unit = { key, value -> put(key, value) },
): T {
    sourceKeys.forEach { key ->
        destination.destinationSetter(key, sourceGetter(key))
    }

    return destination
}

@Suppress("UNCHECKED_CAST")
public fun <T : Any> Any.minusKeys(
    vararg sourceKeys: Any?,
    sourceIterator: Any.() -> Iterator<Map.Entry<Any?, Any?>> = Any::iterator,
    destination: T = toNewMutableCollection() as T,
    destinationSetter: T.(key: Any?, value: Any?) -> Unit = { key, value -> put(key, value) },
): T {
    sourceIterator().forEach { (key, value) ->
        if (key in sourceKeys) return@forEach

        destination.destinationSetter(key, value)
    }

    return destination
}

////////////////////////////////////////////////////////////DEEP////////////////////////////////////////////////////////
@Suppress("UNCHECKED_CAST")
public fun <K> Any.deepGet(vararg path: K, getter: List<Pair<Any, K>>.() -> Any?): Pair<List<Pair<Any, K>>, Any?> {
    var sources = emptyList<Pair<Any, K>>()

    val value = DeepRecursiveFunction<Any, Any?> { source ->
        if (sources.size >= path.size) return@DeepRecursiveFunction source

        sources = sources + (source to path[sources.size])

        sources.getter()?.let { value -> callRecursive(value) }
    }(this)

    return sources to value
}

@Suppress("UNCHECKED_CAST")
public fun <K> Any.deepGetOrElse(vararg path: K, defaultValue: Any.(Any?) -> Any?): Pair<List<Pair<Any, K>>, Any?> =
    deepGet(*path) { last().first.getOrElse(last().second, defaultValue) }

@Suppress("UNCHECKED_CAST")
public fun <K> Any.deepGetOrNull(vararg path: K): Pair<List<Pair<Any, K>>, Any?> =
    deepGet(*path) { last().first.getOrNull(last().second) }

public fun <V, K> Any.deepRunOnPenultimate(
    vararg path: K,
    getter: List<Pair<Any, K>>.() -> Any? = { last().first.getOrNull(last().second) },
    run: List<Pair<Any, K>>.(path: Array<out K>) -> V
): V = deepGet(*path.copyOfRange(0, path.size - 1), getter = getter).let { (sources, value) ->
    sources + (value!! to path[sources.size])
}.run(path)

@Suppress("UNCHECKED_CAST")
public fun <K> Any.deepSet(
    vararg path: K,
    getter: List<Pair<Any, K>>.() -> Any? = {
        last().first.getOrPut(last().second) { key -> put(key, mutableMapOf<Any?, Any?>()) }
    },
    setter: List<Pair<Any, K>>.(path: Array<out K>) -> Unit,
): Unit = deepRunOnPenultimate(*path, getter = getter, run = setter)

public fun <K> Any.deepContains(
    vararg path: K,
    getter: List<Pair<Any, K>>.() -> Any? = { last().first.getOrNull(last().second) },
    contains: List<Pair<Any, K>>.(path: Array<out K>) -> Boolean = { path ->
        size == path.size && last().first.containsKey(last().second)
    },
): Boolean = deepRunOnPenultimate(*path, getter = getter, run = contains)

public fun Any.flattenKeys(
    iteratorOrNull: List<Pair<Any, Any?>>.(value: Any) -> Iterator<Map.Entry<Any?, Any?>>? = { value ->
        value.iteratorOrNull()
    },
): Sequence<Pair<List<Pair<Any, Any?>>, Any?>> = sequence {
    suspend fun SequenceScope<Pair<List<Pair<Any, Any?>>, Any?>>.flattenKeys(
        sources: List<Pair<Any, Any?>>,
        iterator: Iterator<Map.Entry<Any?, Any?>>,
    ) {
        iterator.forEach { (key, value) ->
            val currentSources = sources.replaceLast { copy(second = key) }

            if (value != null)
                currentSources.iteratorOrNull(value)?.also { nextSourceIterator ->
                    return@forEach flattenKeys(currentSources + (value to null), nextSourceIterator)
                }

            yield(currentSources to value)
        }
    }

    flattenKeys(
        listOf(this@flattenKeys to null),
        emptyList<Pair<Any, Any?>>().iteratorOrNull(this@flattenKeys)!!,
    )
}

@Suppress("UNCHECKED_CAST")
public fun <E, K> Collection<Pair<List<E>, Any?>>.unflattenKeys(
    sourceKey: (E) -> K = { sourceKey -> sourceKey as K },
    destinationGetter: List<Pair<Any, K>>.(sourcesKeys: List<E>) -> Any = { _ ->
        if (isEmpty()) mutableMapOf<Any?, Any?>()
        else last().first.getOrPut(last().second) { key -> put(key, mutableMapOf()) }
    },
    destinationSetter: List<Pair<Any, K>>.(value: Any?) -> Unit = { value ->
        last().first.put(last().second, value)
    },
): Any = DeepRecursiveFunction<UnflattenKeysArgs<E, K>, Any> { (sources, destinations) ->
    val (keysPaths, paths) = sources.partition { (sourcePath, _) -> sourcePath.size == 1 }

    val nextDestination = destinations.destinationGetter(sources.map { source -> source.first[0] })

    keysPaths.forEach { (keyPath, value) ->
        val currentDestinations = destinations + (nextDestination to sourceKey(keyPath[0]))

        currentDestinations.destinationSetter(value)
    }

    paths.groupBy { (sourcePath, _) -> sourcePath[0] }.forEach { (key, sources) ->
        callRecursive(
            UnflattenKeysArgs(
                sources.map { source -> source.copy(first = source.first.drop()) },
                destinations + (nextDestination to sourceKey(key)),
            )
        )
    }

    nextDestination
}(UnflattenKeysArgs(toList(), emptyList()))

private data class UnflattenKeysArgs<E, K>(
    val sources: List<Pair<List<E>, Any?>>,
    val destinations: List<Pair<Any, K>>,
)

public fun <K> Collection<Pair<List<Pair<Any, K>>, Any?>>.unflattenKeys(
    destinationGetter: List<Pair<Any, K>>.(sourcesKeys: List<Pair<Any, K>>) -> Any = { sourcesKeys ->
        if (isEmpty()) sourcesKeys[0].first.toNewMutableCollection()
        else last().first.getOrPut(last().second) { key ->
            put(key, sourcesKeys[0].first.toNewMutableCollection())
        }
    },
    destinationSetter: List<Pair<Any, K>>.(value: Any?) -> Unit = { value ->
        last().first.put(last().second, value)
    },
): Any = unflattenKeys({ (_, key) -> key }, destinationGetter, destinationSetter)

@Suppress("UNCHECKED_CAST")
public fun <T : Any> Any.deepMap(
    sourceIteratorOrNull: List<Pair<Any, Any?>>.(value: Any) -> Iterator<Map.Entry<Any?, Any?>>? = { value ->
        value.iteratorOrNull()
    },
    sourceTransform: List<Pair<Any, Any?>>.(value: Any?) -> Pair<Any?, Any?>? = { value -> last().second to value },
    destination: T = toNewMutableCollection() as T,
    destinationGetter: List<Pair<Any, Any?>>.(source: Any) -> Any = { source ->
        last().first.getOrPut(last().second) { key -> put(key, source.toNewMutableCollection()) }.apply {
            (this as? MutableList<*>)?.clear()
        }
    },
    destinationSetter: List<Pair<Any, Any?>>.(value: Any?) -> Unit = { value ->
        last().first.put(last().second, value)
    },
): T {
    DeepRecursiveFunction<DeepMapToArgs, Unit> { (sources, sourceIterator, destinations) ->
        sourceIterator.forEach { (key, value) ->
            val currentSources = sources.replaceLast { copy(second = key) }

            val (key, value) = currentSources.sourceTransform(value) ?: return@forEach

            val currentDestinations = destinations.replaceLast { copy(second = key) }

            if (value != null)
                currentSources.sourceIteratorOrNull(value)?.let { nextSourceIterator ->
                    val nextDestination: Any = currentDestinations.destinationGetter(value)

                    return@forEach callRecursive(
                        DeepMapToArgs(
                            currentSources + (value to null),
                            nextSourceIterator,
                            currentDestinations + (nextDestination to null),

                            ),
                    )
                }

            currentDestinations.destinationSetter(value)
        }
    }(
        DeepMapToArgs(
            listOf(this to null),
            emptyList<Pair<Any, Any?>>().sourceIteratorOrNull(this)!!,
            listOf(destination to null),
        ),
    )

    return destination
}

private data class DeepMapToArgs(
    val sources: List<Pair<Any, Any?>>,
    val sourceIterator: Iterator<Map.Entry<Any?, Any?>>,
    val destinations: List<Pair<Any, Any?>>,
)

@Suppress("UNCHECKED_CAST")
public fun <T : Any> Any.deepMapKeys(
    sourceIteratorOrNull: List<Pair<Any, Any?>>.(value: Any) -> Iterator<Map.Entry<Any?, Any?>>? = { value ->
        value.iteratorOrNull()
    },
    sourceFilter: List<Pair<Any, Any?>>.(value: Any?) -> Boolean = { true },
    sourceTransform: List<Pair<Any, Any?>>.(value: Any?) -> Any?,
    destination: T = toNewMutableCollection() as T,
    destinationGetter: List<Pair<Any, Any?>>.(source: Any) -> Any = { source ->
        last().first.getOrPut(last().second) { key -> put(key, source.toNewMutableCollection()) }.apply {
            (this as? MutableList<*>)?.clear()
        }
    },
    destinationSetter: List<Pair<Any, Any?>>.(value: Any?) -> Unit = { value ->
        last().first.put(last().second, value)
    },
): T = deepMap(
    sourceIteratorOrNull,
    { value ->
        if (sourceFilter(value)) sourceTransform(value) to value else null
    },
    destination,
    destinationGetter,
    destinationSetter,
)

@Suppress("UNCHECKED_CAST")
public fun <T : Any> Any.deepMapValues(
    sourceIteratorOrNull: List<Pair<Any, Any?>>.(value: Any) -> Iterator<Map.Entry<Any?, Any?>>? = { value ->
        value.iteratorOrNull()
    },
    sourceFilter: List<Pair<Any, Any?>>.(value: Any?) -> Boolean = { true },
    sourceTransform: List<Pair<Any, Any?>>.(value: Any?) -> Any?,
    destination: T = toNewMutableCollection() as T,
    destinationGetter: List<Pair<Any, Any?>>.(source: Any) -> Any = { source ->
        last().first.getOrPut(last().second) { key -> put(key, source.toNewMutableCollection()) }.apply {
            (this as? MutableList<*>)?.clear()
        }
    },
    destinationSetter: List<Pair<Any, Any?>>.(value: Any?) -> Unit = { value ->
        last().first.put(last().second, value)
    },
): T = deepMap(
    sourceIteratorOrNull,
    { value ->
        if (sourceFilter(value)) last().second to sourceTransform(value) else null
    },
    destination,
    destinationGetter,
    destinationSetter,
)

@Suppress("UNCHECKED_CAST")
public fun <T : Any, P : Any> Any.deepSlice(
    vararg sourcePaths: P,
    sourcePathKey: List<Pair<Any, Any?>>.(sourcePath: P) -> Any?,
    sourceSubPaths: List<Pair<Any, Any?>>.(value: Any, sourcePath: P) -> List<P>,
    sourceGetter: List<Pair<Any, Any?>>.() -> Any? = { last().first.getOrNull(last().second) },
    destination: T = toNewMutableCollection() as T,
    destinationGetter: List<Pair<Any, Any?>>.(source: Any) -> Any = { source ->
        last().first.getOrPut(last().second) { key -> put(key, source.toNewMutableCollection()) }
    },
    destinationSetter: List<Pair<Any, Any?>>.(value: Any?) -> Unit = { value ->
        last().first.put(last().second, value)
    },
): T {
    DeepRecursiveFunction<DeepSliceArgs<P>, Unit> { (sourcePaths, sources, destinations) ->
        sourcePaths.groupBy { path -> sources.sourcePathKey(path) }.map { (key, paths) ->
            val currentSources = sources.replaceLast { copy(second = key) }
            val currentDestinations = destinations.replaceLast { copy(second = key) }

            val value = currentSources.sourceGetter()

            val nextSourcePaths = if (value == null) emptyList() else paths.flatMap { path ->
                currentSources.sourceSubPaths(value, path)
            }

            if (nextSourcePaths.isEmpty()) return@map currentDestinations.destinationSetter(value)

            val nextDestination = currentDestinations.destinationGetter(value!!)

            callRecursive(
                DeepSliceArgs(
                    nextSourcePaths,
                    currentSources + (value to null),
                    currentDestinations + (nextDestination to null),
                )
            )
        }
    }(
        DeepSliceArgs(
            sourcePaths.toList(),
            listOf(this to null),
            listOf(destination to null),
        )
    )

    return destination
}

private data class DeepSliceArgs<P : Any>(
    val sourcePaths: List<P>,
    val sources: List<Pair<Any, Any?>>,
    val destinations: List<Pair<Any, Any?>>,
)

@Suppress("UNCHECKED_CAST")
public fun <T : Any> Any.deepSlice(
    vararg sourcePaths: List<Any?>,
    sourceGetter: List<Pair<Any, Any?>>.() -> Any? = { last().first.getOrNull(last().second) },
    destination: T = toNewMutableCollection() as T,
    destinationGetter: List<Pair<Any, Any?>>.(source: Any) -> Any = { source ->
        last().first.getOrPut(last().second) { key -> put(key, source.toNewMutableCollection()) }
    },
    destinationSetter: List<Pair<Any, Any?>>.(value: Any?) -> Unit = { value ->
        last().first.put(last().second, value)
    },
): T = deepSlice(
    *sourcePaths,
    sourcePathKey = { sourcePath -> sourcePath.first() },
    sourceSubPaths = { _, sourcePath ->
        sourcePath.drop().let { path -> if (path.isEmpty()) emptyList() else listOf(path) }
    },
    sourceGetter = sourceGetter,
    destination = destination,
    destinationGetter = destinationGetter,
    destinationSetter = destinationSetter
)

@Suppress("UNCHECKED_CAST")
public fun <T : Any, P : Any> Any.deepMinusKeys(
    vararg sourcePaths: P,
    sourcePathKey: List<Pair<Any, Any?>>.(sourcePath: P) -> Any?,
    sourceSubPaths: List<Pair<Any, Any?>>.(value: Any, sourcePath: P) -> List<P>,
    sourceIteratorOrNull: List<Pair<Any, Any?>>.(source: Any) -> Iterator<Map.Entry<Any?, Any?>>? = { source ->
        source.iteratorOrNull()
    },
    destination: T = toNewMutableCollection() as T,
    destinationGetter: List<Pair<Any, Any?>>.(source: Any) -> Any = { source ->
        last().first.getOrPut(last().second) { key -> put(key, source.toNewMutableCollection()) }
    },
    destinationSetter: List<Pair<Any, Any?>>.(value: Any?) -> Unit = { value ->
        last().first.put(last().second, value)
    },
): T {
    DeepRecursiveFunction<DeepMinusKeysArgs<P>, Unit> { (sourcePaths, sources, sourceIterator, destinations) ->
        val keysPaths = sourcePaths.groupBy { path -> sources.sourcePathKey(path) }

        sourceIterator.forEach { (key, value) ->
            val currentSources = sources.replaceLast { copy(second = key) }
            val currentDestinations = destinations.replaceLast { copy(second = key) }

            if (key !in keysPaths) return@forEach currentDestinations.destinationSetter(value)

            if (value == null) return@forEach

            val nextSourcePaths = keysPaths[key]!!.flatMap { path -> currentSources.sourceSubPaths(value, path) }

            if (nextSourcePaths.isEmpty()) return@forEach

            currentSources.sourceIteratorOrNull(value)?.let { nextSourceIterator ->
                val nextDestination: Any = currentDestinations.destinationGetter(value)

                callRecursive(
                    DeepMinusKeysArgs(
                        nextSourcePaths,
                        currentSources + (value to null),
                        nextSourceIterator,
                        currentDestinations + (nextDestination to null),
                    )
                )
            }
        }
    }(
        DeepMinusKeysArgs(
            sourcePaths.toList(),
            listOf(this to null),
            emptyList<Pair<Any, Any?>>().sourceIteratorOrNull(this)!!,
            listOf(destination to null),
        )
    )

    return destination
}

private data class DeepMinusKeysArgs<P : Any>(
    val sourcePaths: List<P>,
    val sources: List<Pair<Any, Any?>>,
    val sourceIterator: Iterator<Map.Entry<Any?, Any?>>,
    val destinations: List<Pair<Any, Any?>>,
)

@Suppress("UNCHECKED_CAST")
public fun <T : Any> Any.deepMinusKeys(
    vararg sourcePaths: List<Any?>,
    sourceIteratorOrNull: List<Pair<Any, Any?>>.(source: Any) -> Iterator<Map.Entry<Any?, Any?>>? = { source ->
        source.iteratorOrNull()
    },
    destination: T = toNewMutableCollection() as T,
    destinationGetter: List<Pair<Any, Any?>>.(source: Any) -> Any = { source ->
        last().first.getOrPut(last().second) { key -> put(key, source.toNewMutableCollection()) }
    },
    destinationSetter: List<Pair<Any, Any?>>.(value: Any?) -> Unit = { value ->
        last().first.put(last().second, value)
    },
): T = deepMinusKeys(
    *sourcePaths,
    sourcePathKey = { sourcePath -> sourcePath.first() },
    sourceSubPaths = { _, sourcePath ->
        sourcePath.drop().let { path -> if (path.isEmpty()) emptyList() else listOf(path) }
    },
    sourceIteratorOrNull = sourceIteratorOrNull,
    destination = destination,
    destinationGetter = destinationGetter,
    destinationSetter = destinationSetter
)