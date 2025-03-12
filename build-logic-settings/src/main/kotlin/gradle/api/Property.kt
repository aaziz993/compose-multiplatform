package gradle.api

import java.io.File
import kotlin.reflect.KMutableProperty0
import org.gradle.api.file.FileSystemLocation
import org.gradle.api.file.FileSystemLocationProperty
import org.gradle.api.provider.HasMultipleValues
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.assign

internal fun trySetSystemProperty(key: String, value: String) {
    if (System.getProperty(key) == null)
        System.setProperty(key, value)
}


public infix fun <T> KMutableProperty0<T>.trySet(value: T?): Unit? = value?.let(::set)

public infix fun <T : FileSystemLocation> FileSystemLocationProperty<T>.tryAssign(file: File?): Unit? =
    file?.let(::assign)

public infix fun <T : FileSystemLocation> FileSystemLocationProperty<T>.tryAssign(provider: Provider<File?>): Unit? =
    provider.takeIf(Provider<File?>::isPresent)?.let(::assign)

public infix fun <T> HasMultipleValues<T>.tryAssign(elements: Iterable<T?>?): Unit? = elements?.let(::assign)

public infix fun <T> HasMultipleValues<T>.tryAssign(provider: Provider<out Iterable<T?>?>): Unit? =
    provider.takeIf(Provider<out Iterable<T?>?>::isPresent)?.let(::assign)

public infix fun <K, V> MapProperty<K, V>.tryAssign(entries: Map<out K?, V?>?): Unit? = entries?.let(::assign)

public infix fun <K, V> MapProperty<K, V>.tryAssign(provider: Provider<out Map<out K?, V?>?>): Unit? =
    provider.takeIf(Provider<out Map<out K?, V?>?>::isPresent)?.let(::assign)

public infix fun <T> Property<T>.tryAssign(value: T?): Unit? = value?.let(::assign)

public infix fun <T> Property<T>.tryAssign(value: Provider<out T?>): Unit? =
    value.takeIf(Provider<out T?>::isPresent)?.let(::assign)
