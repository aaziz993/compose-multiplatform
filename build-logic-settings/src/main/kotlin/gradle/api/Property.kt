package gradle.api

import java.io.File
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KFunction1
import org.gradle.api.Action
import org.gradle.api.file.FileSystemLocation
import org.gradle.api.file.FileSystemLocationProperty
import org.gradle.api.provider.HasMultipleValues
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.assign

public fun trySetSystemProperty(key: String, value: String) {
    if (System.getProperty(key) == null)
        System.setProperty(key, value)
}

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

public fun <E> KMutableProperty0<out MutableCollection<E>?>.addAll(value: Collection<E>) {
    this as KMutableProperty0<MutableCollection<E>?>
    get()?.addAll(value) ?: set(value as MutableCollection<E>)
}

public infix fun <E> KMutableProperty0<out MutableCollection<E>?>.tryAddAll(value: Collection<E>?): Unit? =
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

public infix fun <T : FileSystemLocation> FileSystemLocationProperty<T>.tryAssign(file: File?): Unit? =
    file?.let(::assign)

public infix fun <T : FileSystemLocation> FileSystemLocationProperty<T>.tryAssign(provider: Provider<File?>): Unit? =
    provider.takeIf(Provider<File?>::isPresent)?.let(::assign)

public infix fun <T> HasMultipleValues<T>.tryAssign(elements: Iterable<T?>?): Unit? = elements?.let(::assign)

public infix fun <T> HasMultipleValues<T>.tryAssign(provider: Provider<out Iterable<T?>?>): Unit? =
    provider.takeIf(Provider<out Iterable<T?>?>::isPresent)?.let(::assign)

public infix fun <T> HasMultipleValues<T>.tryAddAll(elements: Iterable<T?>?): Unit? = elements?.let(::addAll)

public infix fun <K, V> MapProperty<K, V>.tryPutAll(entries: Map<out K?, V?>?): Unit? = entries?.let(::putAll)

public infix fun <K, V> MapProperty<K, V>.tryAssign(entries: Map<out K?, V?>?): Unit? = entries?.let(::assign)

public infix fun <K, V> MapProperty<K, V>.tryAssign(provider: Provider<out Map<out K?, V?>?>): Unit? =
    provider.takeIf(Provider<out Map<out K?, V?>?>::isPresent)?.let(::assign)

public infix fun <T> Property<T>.tryAssign(value: T?): Unit? = value?.let(::assign)

public infix fun <T> Property<T>.tryAssign(value: Provider<out T?>): Unit? =
    value.takeIf(Provider<out T?>::isPresent)?.let(::assign)

public infix fun <T> KFunction1<T, Unit>.apply(block: KFunction1<T, Unit>) {
    call(block)
}

public inline infix fun <reified T> KFunction1<Array<T>, *>.trySet(elements: Iterable<T>?) =
    elements?.toList()?.toTypedArray()?.let(::invoke)

public infix fun <T> KFunction1<Iterable<T>, *>.trySet(elements: Iterable<T>?) =
    elements?.let(::invoke)

public infix fun <T> KFunction1<T.() -> Unit, *>.tryApply(block: ((T) -> Unit)?) =
    block?.let { block ->
        invoke(block::invoke)
    }

public infix fun <T> KFunction1<Action<T>, *>.tryApply(block: Action<T>?) =
    block?.let { block ->
        invoke(block::execute)
    }


