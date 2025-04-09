package klib.data.type

import com.ionspin.kotlin.bignum.integer.Quadruple
import klib.data.type.collection.get

public inline fun <T> T.act(action: () -> Unit): T {
    action()
    return this
}

public inline fun <reified T : Any> Any.toNumberOrNull(): T = when (T::class) {
    UByte::class -> toString().toUByte()
    UShort::class -> toString().toUShort()
    UInt::class -> toString().toUInt()
    ULong::class -> toString().toULong()
    Byte::class -> toString().toByte()
    Short::class -> toString().toShort()
    Int::class -> toString().toInt()
    Long::class -> toString().toLong()
    Float::class -> toString().toFloat()
    Double::class -> toString().toDouble()
    else -> null
} as T

@Suppress("UNCHECKED_CAST")
public fun <T : Any, K> T.get(
    vararg keys: K,
    getter: Any.(K) -> Any? = { key -> get(key) }
): Any? = DeepRecursiveFunction<Triple<Any, List<K>, Any.(K) -> Any?>, Any?> { (source, subKeys, getter) ->
    val value = source.getter(subKeys.first())

    if (subKeys.size > 1 && value != null) {
        return@DeepRecursiveFunction callRecursive(Triple(value, keys.drop(1), getter))
    }

    value
}(Triple(this, keys.toList(), getter))

@Suppress("UNCHECKED_CAST")
public fun <T : Any> T.eval(
    context: Any,
    getter: Any.(keys: List<String>) -> Any? = { keys -> get(*keys.toTypedArray()) }
): T = evalDeepRecursiveFunction(Quadruple(this, this, context, getter)) as T

private val evalDeepRecursiveFunction =
    DeepRecursiveFunction<Quadruple<Any, Any, Any, Any.(keys: List<String>) -> Any?>, Any?>
    { (rootReceiver, receiver, context, getter) ->
        when (receiver) {
            is String -> receiver.eval(context, rootReceiver, getter = getter)
            is List<*> -> receiver.map { value -> value?.let { callRecursive(Quadruple(rootReceiver, it, context, getter)) } }
            is Map<*, *> -> receiver.mapValues { (_, value) -> value?.let { callRecursive(Quadruple(rootReceiver, it, context, getter)) } }
            else -> receiver
        }
    }

public fun String.eval(
    context: Any,
    vararg contexts: Any,
    getter: Any.(keys: List<String>) -> Any? = { keys -> get(*keys.toTypedArray()) }
): Any? = if (startsWith("$"))
    removePrefix("$").split(".").let { keys ->
        (listOf(context) + contexts).firstNotNullOfOrNull { context -> context.getter(keys) }
    }
else this

