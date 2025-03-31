package gradle.reflect

import kotlin.reflect.KFunction1
import org.gradle.api.Action

public infix fun <T> KFunction1<T, *>.trySet(elements: T?): Any? =
    elements?.let(::invoke)

@JvmName("trySetArray")
public inline infix fun <reified T> KFunction1<Array<T>, *>.trySet(elements: Iterable<T>?): Any? =
    elements?.toList()?.toTypedArray()?.let(::invoke)

@JvmName("trySetIterable")
public infix fun <T> KFunction1<Iterable<T>, *>.trySet(elements: Iterable<T>?): Any? =
    elements?.let(::invoke)

@JvmName("tryApplyAction")
public infix fun <T> KFunction1<Action<T>, *>.tryApply(block: ((T) -> Unit)?): Any? =
    block?.let { block ->
        invoke(block)
    }

@JvmName("tryApplyFunction")
public infix fun <T> KFunction1<KFunction1<T, Unit>, *>.tryApply(block: ((T) -> Unit)?): Any? =
    block?.let { block ->
        invoke(block::invoke)
    }
