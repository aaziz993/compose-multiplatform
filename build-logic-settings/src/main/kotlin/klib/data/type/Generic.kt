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
): Any? {
    val value = getter(keys.first())

    if (keys.size > 1 && value != null) {
        return value.get(*arrayOf(keys.drop(1)), getter)
    }

    return value
}

@Suppress("UNCHECKED_CAST")
public fun <T : Any> T.resolve(
    mainSource: Any,
    getter: Any.(keys: List<String>) -> Any? = { keys -> get(*keys.toTypedArray()) }
): T = DeepRecursiveFunction<Any, Any?> { receiver ->
    when (receiver) {
        is String -> receiver.resolveValue(mainSource, this@resolve, getter = getter)
        is List<*> -> receiver.map { value -> value?.let { callRecursive(it) } }
        is Map<*, *> -> receiver.mapValues { (_, value) -> value?.let { callRecursive(it) } }
        else -> receiver
    }
}(this) as T

public fun String.resolveValue(
    mainSource: Any,
    vararg sources: Any,
    getter: Any.(keys: List<String>) -> Any?
): Any? = if (startsWith("$"))
    removePrefix("$").split(".").let { keys ->
        (listOf(mainSource) + sources).firstNotNullOfOrNull { source -> source.getter(keys) }
    }
else this

