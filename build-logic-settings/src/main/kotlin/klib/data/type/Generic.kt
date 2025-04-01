package klib.data.type

import klib.data.type.collection.get

public inline fun <T> T.act(action: () -> Unit): T {
    action()
    return this
}

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
public fun <T : Any> T.evalValues(
    thisSource: Any,
    getter: Any.(keys: List<String>) -> Any? = { keys -> get(*keys.toTypedArray()) }
): T = DeepRecursiveFunction<Any, Any?> { receiver ->
    when (receiver) {
        is String -> receiver.eval(thisSource, this@resolve, getter = getter)
        is List<*> -> receiver.map { value -> value?.let { callRecursive(it) } }
        is Map<*, *> -> receiver.mapValues { (_, value) -> value?.let { callRecursive(it) } }
        else -> receiver
    }
}(this) as T

public fun String.eval(
    thisSource: Any,
    vararg sources: Any,
    getter: Any.(keys: List<String>) -> Any?
): Any? = if (startsWith("$"))
    removePrefix("$").split(".").let { keys ->
        (listOf(thisSource) + sources).firstNotNullOfOrNull { source -> source.getter(keys) }
    }
else this

