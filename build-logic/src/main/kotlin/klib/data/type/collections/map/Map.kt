package klib.data.type.collections.map

import klib.data.type.toInt

@Suppress("UNCHECKED_CAST")
public val Any.asNullableMapOrNull: Map<String, Any?>?
    get() = this as? Map<String, Any?>

public val Any.asNullableMap: Map<String, Any?>
    get() = asNullableMapOrNull!!

@Suppress("UNCHECKED_CAST")
public val Any.asMapOrNull: Map<String, Any>?
    get() = this as? Map<String, Any>

public val Any.asMap: Map<String, Any>
    get() = asMapOrNull!!

@Suppress("UNCHECKED_CAST")
public fun <K, V> Any.asMapOrNull(): Map<K, V>? = this as? Map<K, V>

public fun <K, V> Any.asMap(): Map<K, V> = asMapOrNull()!!

public val <K, V> Map<K, V>.pairs: Set<Pair<K, V>>
    get() = entries.map(Map.Entry<K, V>::toPair).toSet()

public val <T> Map<*, T>.valuesByKeysAsIndices: List<T>
    get() = entries.sortedBy { (key, _) -> key!!.toInt() }
        .map(Map.Entry<*, T>::value)

public infix fun <K, V> Map<K, V>.tryPlus(map: Map<K, V>?): Map<K, V> =
    map?.let(::plus) ?: this

public fun <K, V> Map<K, V>.takeIfNotEmpty(): Map<K, V>? = takeIf(Map<K, V>::isNotEmpty)

public fun <K : V, V> Map<K, V>.getOrKey(key: K): V = this[key] ?: key

public infix fun <K, V> Map<K, V>.slice(keys: Iterable<K>): Map<K, V> =
    keys.filter(::containsKey).associateWith(::get).asMap()
