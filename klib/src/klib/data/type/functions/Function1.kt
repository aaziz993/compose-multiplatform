package klib.data.type.functions

import org.gradle.api.Action

public infix fun <T> Function1<T, *>.tryInvoke(value: T?): Any? =
    value?.let(::invoke)

@JvmName("invokeArray")
public inline operator fun <reified E> Function1<Array<E>, *>.invoke(elements: Iterable<E>): Any? =
    invoke(elements.toList().toTypedArray())

@JvmName("tryInvokeArray")
public inline infix fun <reified E> Function1<Array<E>, *>.tryInvoke(elements: Iterable<E>?): Any? =
    elements?.let(::invoke)

@JvmName("tryInvokeLambda")
public infix fun <T> Function1<Function1<T, Unit>, *>.tryInvoke(block: ((T) -> Unit)?): Any? =
    block?.let(::invoke)

@JvmName("tryInvokeAction")
public infix fun <T : Any> Function1<Action<T>, *>.tryInvoke(block: ((T) -> Unit)?): Any? =
    block?.let { block ->
        invoke(block)
    }
