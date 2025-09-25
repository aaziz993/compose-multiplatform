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
public fun <K, V> Any.asMutableMapOrNull(): MutableMap<K, V>? = this as? MutableMap<K, V>

public fun <K, V> Any.asMutableMap(): MutableMap<K, V> = asMutableMapOrNull()!!

public infix fun <K, V> MutableMap<K, V>.tryPutAll(value: Map<K, V>?): Unit? =
    value?.let(::putAll)

public infix fun <K, V> MutableMap<K, V>.updateAll(value: Map<K, V>): Unit = putAll(value.act(::clear))

public infix fun <K, V> MutableMap<K, V>.tryUpdateAll(value: Map<K, V>?): Unit? = value?.let(::updateAll)
