package gradle

@Suppress("UNCHECKED_CAST")
internal infix fun Map<String, Any?>.deepMerge(source: Map<String, Any?>): Map<String, Any?> {
    val resultMap = toMutableMap()
    for (key in source.keys) {
        //recursive merge for nested maps
        when {
            source[key] is Map<*, *> && (resultMap[key] is Map<*, *> || resultMap[key] == null) -> {
                val originalChild = (resultMap[key] as? Map<String, Any?>) ?: mutableMapOf()
                val newChild = source[key] as Map<String, Any?>
                resultMap[key] = originalChild deepMerge newChild
            }

            source[key] is Collection<*> && (resultMap[key] is Collection<*> || resultMap[key] == null) -> {
                val originalChild = (resultMap[key] as? Collection<*>) ?: mutableListOf<Any?>()
                val newChild = source[key] as Collection<*>

                if (!originalChild.containsAll(newChild)) {
                    resultMap[key] = originalChild + newChild
                }
            }

            else -> if (source[key] != null) {
                resultMap[key] = source[key]
            }
        }
    }
    return resultMap
}

internal fun Any.get(vararg keys: Any?) = DeepRecursiveFunction<Pair<List<Any?>, Any>, Any?> { (subKeys, obj) ->
    val key = subKeys.first()

    val value = when (obj) {
        is List<*> -> obj[key.toString().toInt()]
        is Map<*, *> -> obj[key]
        else -> error("Neither list or map to get value by key: $key")
    }

    if (value == null) {
        return@DeepRecursiveFunction value
    }

    subKeys.drop(1).takeIf(List<*>::isNotEmpty)?.let {
        callRecursive(it to value)
    } ?: value
}(keys.toList() to this)
