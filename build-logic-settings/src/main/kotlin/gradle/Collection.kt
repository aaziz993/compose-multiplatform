package gradle

internal fun Any.resolve(): Any? = DeepRecursiveFunction<Any, Any?> { resolvable ->
    when (resolvable) {
        is String -> resolvable.resolveValue()
        is Map<*, *> -> resolvable.mapValues { (_, value) -> value?.let { callRecursive(it) } }
        is List<*> -> resolvable.map { value -> value?.let { callRecursive(it) } }
        else -> this
    }
}(this)

internal fun String.resolveValue(): Any {
    return this
}
