@file:Suppress("UNCHECKED_CAST")

package klib.data.type.reflection

import kotlin.reflect.KMutableProperty0

public infix fun <T> KMutableProperty0<T>.trySet(value: T?): Unit? = value?.let(::set)

public fun <T : Any, V : Any> KMutableProperty0<T?>.trySetOrApply(
    value: V?,
    transform: V.() -> T,
    applyTo: V.(T) -> Unit
): Unit? = value?.let { value ->
    get()?.let { targetValue -> value.applyTo(targetValue) } ?: set(value.transform())
}

public operator fun <E> KMutableProperty0<out Collection<E>?>.plus(value: Iterable<E>) {
    this as KMutableProperty0<Collection<E>?>
    set(get()?.plus(value) ?: value.toList())
}

public infix fun <E> KMutableProperty0<out Collection<E>?>.tryPlus(value: Iterable<E>?): Unit? =
    value?.let(::plus)

public fun <E> KMutableProperty0<out MutableCollection<E>?>.addAll(value: Iterable<E>) {
    this as KMutableProperty0<MutableCollection<E>?>
    get()?.addAll(value) ?: set(value as MutableCollection<E>)
}

public infix fun <E> KMutableProperty0<out MutableCollection<E>?>.tryAddAll(value: Iterable<E>?): Unit? =
    value?.let(::addAll)

public operator fun <K, V> KMutableProperty0<out Map<K, V>?>.plus(value: Map<K, V>) {
    this as KMutableProperty0<Map<K, V>?>
    set(get()?.plus(value) ?: value)
}

public infix fun <K, V> KMutableProperty0<out Map<K, V>?>.tryPlus(value: Map<K, V>?): Unit? =
    value?.let(::plus)

public fun <K, V> KMutableProperty0<out MutableMap<K, V>?>.putAll(value: Map<K, V>) {
    this as KMutableProperty0<MutableMap<K, V>?>
    get()?.putAll(value) ?: set(value as MutableMap<K, V>)
}

public infix fun <K, V> KMutableProperty0<out MutableMap<K, V>?>.tryPutAll(value: Map<K, V>?): Unit? =
    value?.let(::putAll)
