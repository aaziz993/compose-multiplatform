package klib.data.type.collection

import klib.data.type.act

public fun <T> Collection<T>.singleOrAll(): Any? =
    if (size == 1) single() else this

public infix fun <E> MutableCollection<E>.tryAddAll(value: Iterable<E>?): Boolean? =
    value?.let(::addAll)

public infix fun <E> MutableCollection<E>.trySet(value: Iterable<E>?): Boolean? =
    tryAddAll(value?.act(::clear))

@Suppress("UNCHECKED_CAST")
public fun <T : Any, K> T.get(key: K, defaultValue: T.(K) -> Any? = { key -> error("Unknown key '$key' in type: ${this::class::simpleName}") }): Any? = when (this) {
    is List<*> -> key!!.toString().toInt().let { index -> if (index in indices) this[index] else defaultValue(key) }
    is Map<*, *> -> if (containsKey(key)) this[key] else defaultValue(key)
    else -> throw IllegalArgumentException("Unknown type: ${this@get::class::simpleName}")
}
