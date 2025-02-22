package gradle

@Suppress("UNCHECKED_CAST")
internal infix fun Map<String, Any?>.deepMerge(source: Map<String, Any?>): Map<String, Any?> {
    val resultMap = toMutableMap()
    for (key in source.keys) {
        //recursive merge for nested maps
        if (source[key] is Map<*, *> && resultMap[key] is Map<*, *>) {
            val originalChild = resultMap[key] as Map<String, Any?>
            val newChild = source[key] as Map<String, Any?>
            resultMap[key] = originalChild deepMerge newChild
        }
        else if (source[key] is Collection<*> && resultMap[key] is Collection<*>) {
            if (!(resultMap[key] as Collection<*>).containsAll(source[key] as Collection<*>)) {
                resultMap[key] = (resultMap[key] as Collection<*>) + (source[key] as Collection<*>)
            }
        }
        else {
            resultMap[key] = source[key]
        }
    }
    return resultMap
}
