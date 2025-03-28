package gradle.api

import gradle.act
import java.io.File
import kotlin.reflect.KFunction1
import kotlin.reflect.KMutableProperty0
import org.gradle.api.Action
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.FileCollection
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

public infix fun <E> MutableCollection<E>.tryAddAll(value: Iterable<E>?): Boolean? =
    value?.let(::addAll)

public infix fun <E> MutableCollection<E>.trySet(value: Iterable<E>?): Boolean? =
    tryAddAll(value?.act(::clear))

public infix fun <K, V> MutableMap<K, V>.tryPutAll(value: Map<K, V>?): Unit? =
    value?.let(::putAll)

public infix fun <K, V> MutableMap<K, V>.trySet(value: Map<K, V>?): Unit? =
    tryPutAll(value?.act(::clear))

public infix fun ConfigurableFileCollection.from(value: Iterable<*>) =
    from(* value.toList().toTypedArray())

public infix fun ConfigurableFileCollection.tryFrom(value: Iterable<*>?) =
    value?.let(::from)

public infix fun ConfigurableFileCollection.trySetFrom(value: Iterable<*>?) =
    value?.let(::setFrom)

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

public infix fun <T> KFunction1<T, *>.trySet(elements: T?): Any? =
    elements?.let(::invoke)

public inline infix fun <reified T> KFunction1<Array<T>, *>.trySet(elements: Iterable<T>?): Any? =
    elements?.toList()?.toTypedArray()?.let(::invoke)

public infix fun <T> KFunction1<Iterable<T>, *>.trySet(elements: Iterable<T>?): Any? =
    elements?.let(::invoke)

public infix fun <T> KFunction1<T.() -> Unit, *>.tryApply(block: ((T) -> Unit)?): Any? =
    block?.let { block ->
        invoke(block::invoke)
    }

public infix fun <T> KFunction1<Action<T>, *>.tryApply(block: ((T) -> Unit)?): Any? =
    block?.let { block ->
        invoke(block)
    }

public infix fun <T : FileSystemLocation> FileSystemLocationProperty<T>.tryAssign(file: File?): Unit? =
    file?.let(::assign)

public infix fun <T : FileSystemLocation> FileSystemLocationProperty<T>.tryAssign(provider: Provider<File?>): Unit? =
    provider.takeIf(Provider<File?>::isPresent)?.let(::assign)

public infix fun <T> HasMultipleValues<T>.tryAssign(elements: Iterable<T?>?): Unit? = elements?.let(::assign)

public infix fun <T> HasMultipleValues<T>.tryAssign(provider: Provider<out Iterable<T?>?>): Unit? =
    provider.takeIf(Provider<out Iterable<T?>?>::isPresent)?.let(::assign)

public infix fun <T> HasMultipleValues<T>.tryAddAll(elements: Iterable<T?>?): Unit? = elements?.let(::addAll)

public infix fun <T, E> T.trySet(elements: Iterable<E>?): Unit? where T : Provider<out MutableCollection<E>>,
                                                                      T : HasMultipleValues<E> =
    tryAddAll(elements?.act(get()::clear))

public infix fun <K, V> MapProperty<K, V>.tryPutAll(entries: Map<out K?, V?>?): Unit? = entries?.let(::putAll)

public infix fun <K, V> MapProperty<K, V>.tryAssign(entries: Map<out K?, V?>?): Unit? = entries?.let(::assign)

public infix fun <K, V> MapProperty<K, V>.tryAssign(provider: Provider<out Map<out K?, V?>?>): Unit? =
    provider.takeIf(Provider<out Map<out K?, V?>?>::isPresent)?.let(::assign)

public infix fun <T> Property<T>.tryAssign(value: T?): Unit? = value?.let(::assign)

public infix fun <T> Property<T>.tryAssign(value: Provider<out T?>): Unit? =
    value.takeIf(Provider<out T?>::isPresent)?.let(::assign)


