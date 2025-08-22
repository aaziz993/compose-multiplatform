package klib.data.type.collections.map

import klib.data.type.act

@Suppress("UNCHECKED_CAST")
public val Any.asNullableMutableMapOrNull: MutableMap<String, Any?>?
    get() = this as? MutableMap<String, Any?>

public val Any.asNullableMutableMap: MutableMap<String, Any?>
    get() = asNullableMutableMapOrNull!!

@Suppress("UNCHECKED_CAST")
public val Any.asMutableMapOrNull: MutableMap<String, Any>?
    get() = this as? MutableMap<String, Any>

public val Any.asMutableMap: MutableMap<String, Any>
    get() = asMutableMapOrNull!!

@Suppress("UNCHECKED_CAST")
public fun <K, V> Any.asMutableMapOrNull(): Map<K, V>? = this as? Map<K, V>

public fun <K, V> Any.asMutableMap(): Map<K, V> = asMutableMapOrNull()!!

public infix fun <K, V> MutableMap<K, V>.tryPutAll(value: Map<K, V>?): Unit? =
    value?.let(::putAll)

public infix fun <K, V> MutableMap<K, V>.updateAll(value: Map<K, V>): Unit? =
    putAll(value.act(::clear))

public infix fun <K, V> MutableMap<K, V>.tryUpdateAll(value: Map<K, V>?): Unit? =
    value?.let(::updateAll)

public inline fun <K, V> MutableMap<K, V>.getOrPut(key: K, putter: MutableMap<K, V>.(key: K) -> V): V =
    this[key] ?: putter(key)

public inline fun <K, V> MutableMap<K, V>.put(
    key: K,
    value: V,
    overrider: MutableMap<K, V>.(key: K, value: V) -> Unit = { key, value -> put(key, value) },
): V {
    if (containsKey(key)) overrider(key, value) else this[key] = value

    return value
}
