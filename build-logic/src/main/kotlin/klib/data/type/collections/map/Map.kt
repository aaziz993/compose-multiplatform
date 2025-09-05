package klib.data.type.collections.map

@Suppress("UNCHECKED_CAST")
public val Any.asNullableMapOrNull: Map<String, Any?>?
    get() = this as? Map<String, Any?>

public val Any.asStringNullableMap: Map<String, Any?>
    get() = asNullableMapOrNull!!

@Suppress("UNCHECKED_CAST")
public val Any.asMapOrNull: Map<String, Any>?
    get() = this as? Map<String, Any>

public val Any.asMap: Map<String, Any>
    get() = asMapOrNull!!

@Suppress("UNCHECKED_CAST")
public fun <K, V> Any.asMapOrNull(): Map<K, V>? = this as? Map<K, V>

public fun <K, V> Any.asMap(): Map<K, V> = asMapOrNull()!!

public fun <K, V> Map<K, V>.pairs(): Set<Pair<K, V>> = entries.map(Map.Entry<K, V>::toPair).toSet()

public inline fun <K, V, R : Comparable<R>> Map<K, V>.toSortedKeys(
    crossinline selector: (Map.Entry<K, V>) -> R?
): List<K> = entries.sortedBy(selector).map(Map.Entry<K, *>::key)

public inline fun <K, V, R : Comparable<R>> Map<K, V>.toSortedValues(
    crossinline selector: (Map.Entry<K, V>) -> R?
): List<V> = entries.sortedBy(selector).map(Map.Entry<*, V>::value)

public infix fun <K, V> Map<K, V>.tryPlus(map: Map<K, V>?): Map<K, V> =
    map?.let(::plus) ?: this

public fun <K, V> Map<K, V>.takeIfNotEmpty(): Map<K, V>? = takeIf(Map<K, V>::isNotEmpty)

public fun <K : V, V> Map<K, V>.getOrKey(key: K): V = this[key] ?: key

public infix fun <K, V> Map<K, V>.slice(keys: Iterable<K>): Map<K, V> =
    keys.filter(::containsKey).associateWith(::get).asMap()