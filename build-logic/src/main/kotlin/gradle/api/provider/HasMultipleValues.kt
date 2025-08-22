package gradle.api.provider

import org.gradle.api.provider.HasMultipleValues
import org.gradle.kotlin.dsl.assign

public operator fun <T : Any> HasMultipleValues<T>.divAssign(elements: Iterable<T>?): Unit =
    set(elements)

public infix fun <T : Any> HasMultipleValues<T>.tryAssign(elements: Iterable<T>?): Unit? =
    elements?.let(::assign)

public infix fun <T : Any> HasMultipleValues<T>.tryAddAll(elements: Iterable<T>?): Unit? =
    elements?.let(::addAll)
