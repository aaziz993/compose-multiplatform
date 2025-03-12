package gradle.coolection

import kotlin.invoke

internal fun Any.resolve(): Any? = DeepRecursiveFunction<Any, Any?> { obj ->
    when (obj) {
        is String -> obj.resolveValue()
        is Map<*, *> -> obj.mapValues { (_, value) -> value?.let { callRecursive(it) } }
        is List<*> -> obj.map { value -> value?.let { callRecursive(it) } }
        else -> obj
    }
}(this)

internal fun String.resolveValue(): Any {
    return this
}
