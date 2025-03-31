package gradle.api.provider

import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.assign

public infix fun <T> Property<T>.tryAssign(value: T?): Unit? = value?.let(::assign)

public infix fun <T> Property<T>.tryAssign(value: Provider<out T?>): Unit? =
    value.takeIf(Provider<out T?>::isPresent)?.let(::assign)

