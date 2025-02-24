package gradle

@Suppress("UNCHECKED_CAST")
internal infix fun Map<String, Any?>.deepMerge(source: Map<String, Any?>): Map<String, Any?> {
    val resultMap = toMutableMap()
    for (key in source.keys) {
        //recursive merge for nested maps
        when {
            source[key] is Map<*, *> && (resultMap[key] is Map<*, *> || resultMap[key] == null) -> {
                if (resultMap[key] == null) {
                    resultMap[key] = mutableMapOf<String, Any?>()
                }
                val originalChild = resultMap[key] as Map<String, Any?>
                val newChild = source[key] as Map<String, Any?>
                resultMap[key] = originalChild deepMerge newChild
            }

            source[key] is Collection<*> && (resultMap[key] is Collection<*> || resultMap[key] == null) -> {
                if (resultMap[key] == null) {
                    resultMap[key] = mutableListOf<Any?>()
                }

                if (!(resultMap[key] as Collection<*>).containsAll(source[key] as Collection<*>)) {
                    resultMap[key] = (resultMap[key] as Collection<*>) + (source[key] as Collection<*>)
                }
            }

            else -> resultMap[key] = source[key]
        }
    }
    return resultMap
}
