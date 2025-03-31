package gradle.api.provider

import org.gradle.api.provider.HasMultipleValues
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.assign

public infix fun <T> HasMultipleValues<T>.tryAssign(elements: Iterable<T?>?): Unit? = elements?.let(::assign)

public infix fun <T> HasMultipleValues<T>.tryAssign(provider: Provider<out Iterable<T?>?>): Unit? =
    provider.takeIf(Provider<out Iterable<T?>?>::isPresent)?.let(::assign)

public infix fun <T> HasMultipleValues<T>.tryAddAll(elements: Iterable<T?>?): Unit? = elements?.let(::addAll)
