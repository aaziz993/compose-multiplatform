package gradle.api.provider

import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.assign

public operator fun <T : Any> Property<T>.divAssign(value: T?): Unit = set(value)

public infix fun <T : Any> Property<T>.tryAssign(value: T?): Unit? = value?.let(::assign)
