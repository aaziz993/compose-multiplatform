package gradle.reflect

import kotlin.reflect.KMutableProperty0
import org.gradle.api.file.FileCollection

public infix fun <T> KMutableProperty0<T>.trySet(value: T?): Unit? = value?.let(::set)

public fun <T : Any, O : Any> KMutableProperty0<T?>.trySet(
    obj: O?,
    getValue: O.() -> T,
    applyTo: O.(T) -> Unit
): Unit? = obj?.let { obj ->
    get()?.let { value -> obj.applyTo(value) } ?: set(obj.getValue())
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

public operator fun KMutableProperty0<out FileCollection?>.plus(value: FileCollection): Unit {
    this as KMutableProperty0<FileCollection?>
    set(get()?.plus(value) ?: value)
}

public infix fun KMutableProperty0<out FileCollection?>.tryPlus(value: FileCollection?): Unit? =
    value?.let(::plus)
